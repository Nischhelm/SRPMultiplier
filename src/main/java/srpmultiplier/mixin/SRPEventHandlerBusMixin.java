package srpmultiplier.mixin;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.SRPMultiplier;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

import java.util.HashMap;
import java.util.Map;

@Mixin(SRPEventHandlerBus.class)
public abstract class SRPEventHandlerBusMixin {
    @Unique
    HashMap<Integer,Long> lastWake = new HashMap<>();

    @Redirect(
            method = "playerUp",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;setTotalKills(IZLnet/minecraft/world/World;Z)Z"),
            remap = false
    )
    boolean flatSleepPenaltyMixin(SRPWorldData data, int in, boolean plus, World world, boolean canChangePhase){
        if(SRPMultiplierConfigHandler.server.flatSleepPenalty) {
            int dimension = world.provider.getDimension();
            long currentWT = world.getWorldTime();
            if (!lastWake.containsKey(dimension)) {
                lastWake.put(dimension, currentWT);
                return data.setTotalKills(in, true, world, true);
            }

            if (currentWT < lastWake.get(dimension) + 1000) {
                return false;
            } else {
                lastWake.put(dimension, currentWT);
            }
        }
        return data.setTotalKills(in, true, world, true);
    }
}