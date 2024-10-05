package srpmultiplier.mixin;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import net.minecraft.entity.EntityList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;


@Mixin(EntityPStationary.class)
public abstract class EntityPStationaryMixin {
    @Redirect(
            method = "func_70636_d",
            at = @At(value="INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationary;func_70097_a(Lnet/minecraft/util/DamageSource;F)Z"),
            remap = false
    )
    boolean onlyDamageWhitelistedDeterrents(EntityPStationary instance, DamageSource source, float amount){
        ResourceLocation resourcelocation = EntityList.getKey(instance);
        String typeOfThis = resourcelocation == null ? "" : resourcelocation.toString();

        boolean listContainsThis = false;
        for (String entityName : SRPMultiplierConfigHandler.server.whiteListedDeterrents)
            if (typeOfThis.equals(entityName)) {
                listContainsThis = true;
                break;
            }

        if(listContainsThis == SRPMultiplierConfigHandler.server.blackListDeterrents)
            return false;
        else
            return instance.attackEntityFrom(DamageSource.OUT_OF_WORLD, 1.0F);
    }
}