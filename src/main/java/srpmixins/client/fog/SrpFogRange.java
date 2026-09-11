package srpmixins.client.fog;

import srpmixins.SRPMixins;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

public final class SrpFogRange {
    private static SrpFogRange access = create();

    private final Object state;
    private final Field mode;
    private final Field start;
    private final Field end;

    private SrpFogRange() throws ReflectiveOperationException {
        state = ReflectionHelper.findField(GlStateManager.class, "fogState", "field_179155_g").get(null);
        Class<?> stateClass = state.getClass();
        mode = ReflectionHelper.findField(stateClass, "mode", "field_179047_b");
        start = ReflectionHelper.findField(stateClass, "start", "field_179045_d");
        end = ReflectionHelper.findField(stateClass, "end", "field_179046_e");
    }

    private static SrpFogRange create() {
        try {
            return new SrpFogRange();
        } catch (ReflectiveOperationException | RuntimeException exception) {
            SRPMixins.LOGGER.warn("SRP fog range disabled: cannot access Minecraft fog state", exception);
            return null;
        }
    }

    public static void apply(float density) {
        SrpFogRange current = access;
        if (current == null || density <= 0.0F) return;
        float fogStart;
        float fogEnd;
        try {
            if (current.mode.getInt(current.state) != GL11.GL_LINEAR) return;
            fogStart = current.start.getFloat(current.state);
            fogEnd = current.end.getFloat(current.state);
        } catch (ReflectiveOperationException | RuntimeException exception) {
            access = null;
            SRPMixins.LOGGER.warn("SRP fog range disabled: cannot read Minecraft fog state", exception);
            return;
        }
        float scale = SrpFogTransition.rangeScale(density, fogEnd);
        GlStateManager.setFogStart(fogStart * scale);
        GlStateManager.setFogEnd(fogEnd * scale);
    }
}
