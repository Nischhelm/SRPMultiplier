package srpmixins.client.fog;

import net.minecraft.client.Minecraft;

import java.lang.ref.WeakReference;

public final class SrpFogState {
    private static WeakReference<Object> connection = new WeakReference<>(null);
    private static SrpFogTransition transition = new SrpFogTransition();

    private SrpFogState() {
    }

    private static void checkConnection() {
        Object current = Minecraft.getMinecraft().getConnection();
        if (current == null || connection.get() != current) {
            connection = new WeakReference<>(current);
            transition = new SrpFogTransition();
        }
    }

    public static void receive(float density) {
        checkConnection();
        transition.receive(density, System.nanoTime());
    }

    public static float density() {
        checkConnection();
        return transition.sample(System.nanoTime());
    }
}
