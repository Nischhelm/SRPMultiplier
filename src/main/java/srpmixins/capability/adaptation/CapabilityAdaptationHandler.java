package srpmixins.capability.adaptation;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CapabilityAdaptationHandler {
    public static final ResourceLocation CAP_ADAPTATION_KEY = new ResourceLocation(SRPMixins.MODID, "adaptation");

    @CapabilityInject(ICapabilityAdaptation.class)
    public static Capability<ICapabilityAdaptation> CAP_ADAPTATION;

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(ICapabilityAdaptation.class, new Storage(), CapabilityAdaptation::new);
    }

    public static class EventHandler {
        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
            ItemStack stack = event.getObject();
            Item item = stack.getItem();
            if(!(item instanceof WeaponToolArmorBase)) return;

            if(stack.getMaxStackSize() > 1 || stack.getCount() != 1) return;
            if(!SRPMixins.completedLoading) return;
            if(stack.hasCapability(CapabilityAdaptationHandler.CAP_ADAPTATION, null)) return;

            //Move the old values over if there are any
            List<String> oldNames = new ArrayList<>();
            List<Integer> oldCounts = new ArrayList<>();
            if(stack.hasTagCompound()) {
                NBTTagCompound nbt = stack.getTagCompound();
                if (nbt.hasKey("sprresistances")) { //[sic]

                    NBTTagList allResS = nbt.getTagList("sprresistances", 10);
                    NBTTagList allResI = nbt.getTagList("sprresistancei", 10);
                    if (allResS.tagCount() > 0 && allResS.tagCount() == allResI.tagCount()) {
                        for (int i = 0; i < allResS.tagCount(); ++i) {
                            NBTTagCompound nameCmpnd = allResS.getCompoundTagAt(i);
                            NBTTagCompound countCmpnd = allResI.getCompoundTagAt(i);

                            String name = nameCmpnd.getString("resistance" + i);
                            int count = countCmpnd.getInteger("resistance" + i);

                            if(!SRPMixinsConfigHandler.adaptation.fixNullAdaptation || !name.isEmpty()) {
                                oldNames.add(name);
                                oldCounts.add(count);
                            }
                        }
                    }
                    nbt.removeTag("sprresistances");
                    nbt.removeTag("sprresistancei");
                    nbt.removeTag("sprresistanceb");
                    stack.setTagCompound(nbt);
                }
            }

            event.addCapability(CAP_ADAPTATION_KEY, new Provider(event.getObject(), oldNames, oldCounts));

        }

        @SubscribeEvent
        public static void onPlayerHurt(LivingHurtEvent event) {
            if (event.getSource() == null) return;
            if (!(event.getEntityLiving() instanceof EntityPlayer)) return;

            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if(player.inventory.armorInventory.stream().noneMatch(stack -> stack.getItem() instanceof WeaponToolArmorBase))
                return;

            float amount = event.getAmount();
            DamageSource source = event.getSource();
            Entity immediateSource = source.getImmediateSource();

            boolean isFireDamage = SRPMixinsConfigProvider.fireMultiDmgTypes.contains(source.damageType) ||
                    SRPMixinsConfigProvider.fireMultiDmgTypes.contains("isBurning") && player.isBurning();

            if (isFireDamage) {
                amount *= (float) SRPMixinsConfigHandler.adaptation.fireDamageMultiplier;

                // default SRP behavior
                if (SRPMixinsConfigHandler.adaptation.burningAdaptationEffectiveness <= 0.0) {
                    event.setAmount(amount);
                    return;
                }
            }

            float reductionAmount = 0.0F;

            String damageTypeName = "";
            byte blackListType = 0;
            if (immediateSource != null || SRPMixinsConfigHandler.adaptation.fixNullAdaptation) {
                if (immediateSource instanceof EntityPlayer) damageTypeName = immediateSource.getName();
                else if (immediateSource instanceof EntityLivingBase) {
                    ResourceLocation loc = EntityList.getKey(immediateSource);
                    if (loc != null) damageTypeName = loc.toString();
                    else return;
                } else {
                    damageTypeName = source.damageType;
                    blackListType = 2;
                }
            }

            damageTypeName = SRPMixinsConfigProvider.adaptationOverrides.getOrDefault(damageTypeName, damageTypeName);
            if(damageTypeName.equals("NONE")) return;

            for (ItemStack stack : player.inventory.armorInventory) {
                if (!(stack.getItem() instanceof WeaponToolArmorBase)) continue;
                ICapabilityAdaptation adaCap = stack.getCapability(CapabilityAdaptationHandler.CAP_ADAPTATION, null);
                if (adaCap == null) continue;

                boolean hasAdaptation = adaCap.hasAdaptation(damageTypeName);

                if (!isFireDamage) {
                    //Try to add it if it doesn't exist yet
                    if (!hasAdaptation) hasAdaptation = adaCap.tryAddAdaptation(damageTypeName, player.getRNG(), blackListType);

                    if (hasAdaptation) {
                        //Try to increase the adaptation
                        adaCap.tryIncreaseAdaptation(damageTypeName, player.getRNG());
                    }
                }

                // Use existing adaptations (possibly with reduced effectiveness when burning)
                if (hasAdaptation)
                    reductionAmount += adaCap.getReduction(damageTypeName);
            }

            // Apply reduction effectiveness multiplier when burning
            if (isFireDamage && SRPMixinsConfigHandler.adaptation.burningAdaptationEffectiveness < 1.0)
                reductionAmount *= (float) SRPMixinsConfigHandler.adaptation.burningAdaptationEffectiveness;

            event.setAmount(Math.max(amount * (1 - reductionAmount), 0.0F));

            if (immediateSource instanceof EntityLivingBase && SRPConfig.armorCoth)
                SRPPotions.applyStackPotion(SRPPotions.COTH_E, (EntityLivingBase) immediateSource, 400, 2);
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private final ICapabilityAdaptation instance;

        public Provider(ItemStack stack, List<String> oldNames, List<Integer> oldCounts) {
            this.instance = new CapabilityAdaptation(stack, oldNames, oldCounts);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAP_ADAPTATION;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CAP_ADAPTATION ? CAP_ADAPTATION.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) CAP_ADAPTATION.writeNBT(instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            CAP_ADAPTATION.readNBT(instance, null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<ICapabilityAdaptation> {
        @Override
        public NBTBase writeNBT(Capability<ICapabilityAdaptation> capability, ICapabilityAdaptation instance, EnumFacing side) {
            return instance.writeToNBT();
        }

        @Override
        public void readNBT(Capability<ICapabilityAdaptation> capability, ICapabilityAdaptation instance, EnumFacing side, NBTBase nbt) {
            instance.readFromNBT((NBTTagCompound) nbt);
        }
    }
}

