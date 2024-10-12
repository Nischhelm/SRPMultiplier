package srpmultiplier.mixin.features;

import com.dhanantry.scapeandrunparasites.init.SRPItems;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(SRPItems.class)
public abstract class StrangeBonesStack {
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