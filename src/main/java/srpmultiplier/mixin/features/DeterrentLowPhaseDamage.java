package srpmultiplier.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import net.minecraft.entity.EntityList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

import java.util.Arrays;


@Mixin(EntityPStationary.class)
public abstract class DeterrentLowPhaseDamage {
    @Redirect(
            method = "func_70636_d",
            at = @At(value="INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationary;func_70097_a(Lnet/minecraft/util/DamageSource;F)Z"),
            remap = false
    )
    boolean onlyDamageWhitelistedDeterrents(EntityPStationary instance, DamageSource source, float amount){
        ResourceLocation resourcelocation = EntityList.getKey(instance);
        String typeOfThis = resourcelocation == null ? "" : resourcelocation.toString();

        boolean listContainsThis = Arrays.asList(SRPMultiplierConfigHandler.server.whiteListedDeterrents).contains(typeOfThis);
        if(listContainsThis == SRPMultiplierConfigHandler.server.blackListDeterrents)
            return false;
        return instance.attackEntityFrom(DamageSource.OUT_OF_WORLD, 1.0F);
    }
}