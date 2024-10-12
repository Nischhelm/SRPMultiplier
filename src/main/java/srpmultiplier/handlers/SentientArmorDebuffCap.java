package srpmultiplier.handlers;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rlmixins.handlers.ForgeConfigHandler;

public class SentientArmorDebuffCap {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityHurt(LivingHurtEvent event) {
        if(SRPMultiplierConfigHandler.server.fixSentientArmorCuring)
            if (event.getEntity() != null) {
                EntityLivingBase entity = (EntityLivingBase) event.getEntity();
                PotionEffect viral = entity.getActivePotionEffect(SRPPotions.VIRA_E);
                PotionEffect fear = entity.getActivePotionEffect(SRPPotions.FEAR_E);
                if (viral != null && ForgeConfigHandler.server.parasiteArmorViralCuring || fear != null && ForgeConfigHandler.server.parasiteArmorFearCuring) {
                    for (ItemStack stack : entity.getArmorInventoryList()) {
                        if (!(stack.getItem() instanceof WeaponToolArmorBase)) {
                            return;
                        }
                    }

                    if (viral != null && ForgeConfigHandler.server.parasiteArmorViralCuring) {
                        removeAndLimit(entity, viral, ForgeConfigHandler.server.parasiteArmorViralMax);
                    }

                    if (fear != null && ForgeConfigHandler.server.parasiteArmorFearCuring) {
                        removeAndLimit(entity, fear, ForgeConfigHandler.server.parasiteArmorFearMax);
                    }
                }

            }
    }

    private static void removeAndLimit(EntityLivingBase entity, PotionEffect previous, int maxAmp) {
        if (maxAmp < previous.getAmplifier()) {
            entity.removeActivePotionEffect(previous.getPotion());
            if (maxAmp != -1) {
                entity.addPotionEffect(new PotionEffect(previous.getPotion(), previous.getDuration(), maxAmp, previous.getIsAmbient(), previous.doesShowParticles()));
            }

        }
    }


}