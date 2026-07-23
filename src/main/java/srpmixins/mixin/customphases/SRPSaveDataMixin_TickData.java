package srpmixins.mixin.customphases;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.util.IIsTicking;

import java.util.HashMap;
import java.util.Map;

@Mixin(SRPSaveData.class)
public abstract class SRPSaveDataMixin_TickData implements IIsTicking {
    //This class overwrites the original cooldown handling (which was sketchy AF) to use a ticking down cooldown system instead
    //Also adds an inherent timer ticking up

    @Inject(method = "writeToNBT", at = @At("TAIL"))
    private void srpmixins_writeTick(NBTTagCompound compound, CallbackInfoReturnable<NBTTagCompound> cir){
        compound.setLong("srpmixins_tick", srpmixins$tickCount);

        NBTTagList list = new NBTTagList();
        for(Map.Entry<Integer, Integer> entry : srpmixins$cooldowns.entrySet()){
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("dimId", entry.getKey());
            nbt.setLong("cooldown", entry.getValue());
            list.appendTag(nbt);
        }
        compound.setTag("srpmixins_cooldowns", list);
    }

    @Inject(method = "readFromNBT", at = @At("TAIL"))
    private void srpmixins_readTick(NBTTagCompound compound, CallbackInfo ci){
        this.srpmixins$tickCount = compound.getLong("srpmixins_tick");

        if(compound.hasKey("srpmixins_cooldowns")) {
            NBTTagList list = compound.getTagList("srpmixins_cooldowns", 10);
            for (NBTBase nbt : list) {
                NBTTagCompound cmp = (NBTTagCompound) nbt;
                int dim = cmp.getInteger("dimId");
                int cooldown = cmp.getInteger("cooldown");
                srpmixins$cooldowns.put(dim, cooldown);
            }
        }
    }

    @Inject(method = "setCooldown", at = @At("HEAD"), remap = false)
    private void srpmixins_setCooldownTick(int newCooldownSeconds, World world, int dim, CallbackInfo ci){
        if(newCooldownSeconds < 0) newCooldownSeconds = 0;
        this.srpmixins$cooldowns.put(dim, newCooldownSeconds * 20);
    }

    @Inject(method = "getCooldown", at = @At("HEAD"), remap = false, cancellable = true)
    private void srpmixins_getCooldownTick(World worldIn, int dim, CallbackInfoReturnable<Integer> cir){
        if(!SRPMixinsConfigHandler.playerphases.enabled) return;
        cir.setReturnValue(srpmixins$cooldowns.getOrDefault(dim, 0) / 20);
    }

    @Inject(
            method = "setEvolutionPhase",
            at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;set(ILjava/lang/Object;)Ljava/lang/Object;", ordinal = 3),
            remap = false
    )
    private void srpmixins_addPhaseCooldown(int dim, byte phase, boolean doOverride, World world, boolean canChangePhase, CallbackInfoReturnable<Boolean> cir){
        srpmixins$cooldowns.put(dim, srpmixins$cooldowns.getOrDefault(dim, 0) + SRPConfigProvider.getPhaseCooldown(phase) * 20);
    }

    @Unique private long srpmixins$tickCount = 0;
    @Unique private final Map<Integer,Integer> srpmixins$cooldowns = new HashMap<>();

    @Override
    public long srpmixins$getTick() {
        return this.srpmixins$tickCount;
    }

    @Override
    public void srpmixins$setTick(long tick) {
        this.srpmixins$tickCount = tick;
    }

    @Override
    public void srpmixins$tick(int by) {
        this.srpmixins$tickCount += by; //Tick internal clock up

        for(Integer dim : this.srpmixins$cooldowns.keySet()) {
            int cooldown = this.srpmixins$cooldowns.get(dim);
            if(cooldown <= 0) continue;
            this.srpmixins$cooldowns.put(dim, Math.max(cooldown - by, 0)); //tick cooldowns down
        }
    }
}
