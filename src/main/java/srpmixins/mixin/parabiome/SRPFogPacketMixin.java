package srpmixins.mixin.parabiome;

import com.dhanantry.scapeandrunparasites.network.SRPPacketFog;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import srpmixins.client.fog.SrpFogState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SRPPacketFog.Handler.class, remap = false)
public abstract class SRPFogPacketMixin {
    @Redirect(method = "handle", at = @At(value = "FIELD",
            target = "Lcom/dhanantry/scapeandrunparasites/util/handlers/SRPEventHandlerBus;fog:F",
            opcode = Opcodes.PUTSTATIC))
    private void srpmixins$receiveFog(float density) {
        SRPEventHandlerBus.fog = density;
        SrpFogState.receive(density);
    }
}
