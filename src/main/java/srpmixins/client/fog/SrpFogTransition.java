package srpmixins.client.fog;

public final class SrpFogTransition {
    private float value;
    private float target;
    private long sampledAt;
    private boolean initialized;

    public void receive(float density, long now) {
        sample(now);
        target = Float.isFinite(density) ? Math.max(0.0F, density) : 0.0F;
    }

    public float sample(long now) {
        if (initialized) {
            double elapsed = Math.max(0.0, (now - sampledAt) * 1.0e-9);
            value += (target - value) * (float) -Math.expm1(-elapsed / 0.15);
            if (target == 0.0F && value < 1.0e-7F) value = 0.0F;
        }
        sampledAt = now;
        initialized = true;
        return value;
    }

    public static float colorWeight(float density, float maximum) {
        if (!Float.isFinite(maximum) || maximum <= 0.0F) return 0.0F;
        float weight = Math.max(0.0F, Math.min(1.0F, density / maximum / 0.2F));
        return weight * weight * (3.0F - 2.0F * weight);
    }

    public static float rangeScale(float density, float end) {
        return 1.0F / (1.0F + Math.max(0.0F, density) * Math.max(0.0F, end));
    }
}
