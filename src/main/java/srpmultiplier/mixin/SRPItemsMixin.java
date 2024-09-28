package srpmultiplier.mixin;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPItems;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(SRPItems.class)
public abstract class SRPItemsMixin {
    @Shadow(remap = false)
    public static Item bone;

    @Inject(
            method="init",
            at=@At(value="TAIL"),
            remap=false
    )
    private static void forceSpawnerSpawnsMixin(CallbackInfo ci){
        if(SRPMultiplierConfigHandler.server.strangeBonesStack) {
            bone.setMaxStackSize(16);
        }
    }
}