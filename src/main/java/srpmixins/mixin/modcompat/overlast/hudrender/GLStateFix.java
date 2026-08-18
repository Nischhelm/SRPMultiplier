package srpmixins.mixin.modcompat.overlast.hudrender;

import com.overlast.gui.RenderHUD;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderHUD.class)
public abstract class GLStateFix {
    @Inject(method = "renderOverlay", at = @At("HEAD"), remap = false)
    public void srpmixins$fixGLState(RenderGameOverlayEvent event, CallbackInfo ci){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
    }
}
