package srpmultiplier.handlers;

import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public interface SRPWorldDataInterface {

    void setUUID(UUID uuid);
    UUID getUUID();

    SRPWorldData getByPlayer(World world, UUID playerUUID);

    SRPWorldData getByBlock(World world, BlockPos blockPos);
}