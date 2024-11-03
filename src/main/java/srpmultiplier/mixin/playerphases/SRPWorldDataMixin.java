package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.SRPMultiplier;
import srpmultiplier.handlers.PlayerPhases_AlertOnePlayer;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.SRPWorldDataInterface;

import java.util.Arrays;
import java.util.UUID;

@Mixin(SRPWorldData.class)
public abstract class SRPWorldDataMixin implements SRPWorldDataInterface {

    @ModifyArg(
            method = "<init>()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/WorldSavedData;<init>(Ljava/lang/String;)V"),
            remap = false
    )
    private static String mixin(String name){
        return name+uuidtmp;
    }

    /*@Inject(
            method = "create",
            at = @At(value = "HEAD"),
            remap = false
    )
    private static void getServerWorldDataMixin(World world, MapStorage storage, CallbackInfoReturnable<SRPWorldData> cir) {
        SRPMultiplier.LOGGER.info("Creating new SRPWorldData for dim{}", world.provider.getDimension());
        if(world.isRemote) {
            try {
                throw (new Exception("This Method called me"));
            } catch(Exception e){
                e.printStackTrace(System.out);
            }
        }
    }*/

    @Unique
    private static String uuidtmp = "";
    @Unique
    private UUID playerUUID;
    @Override
    public void setUUID(UUID uuid){
        playerUUID = uuid;
    }

    @Unique
    private static SRPWorldData createForPlayer(World world, UUID playerUUID, MapStorage storage) {
        //SRPMultiplier.LOGGER.info("Creating new SRPWorldData for dim{} and player{}", world.provider.getDimension(),playerUUID.toString());
        uuidtmp = playerUUID.toString();
        SRPWorldData instance = new SRPWorldData();
        uuidtmp = "";
        storage.setData("srparasites_data"+playerUUID.toString(), instance);
        int currentDim = world.provider.getDimension();
        instance.setGaining(true);
        instance.setLoss(false);
        instance.setEvolutionPhase(SRPConfigSystems.defaultEvoPhase, true, world, true);
        instance.setTotalKills(SRPConfigSystems.defaultEvoPoints, false, world, true);

        for(String line: SRPConfigSystems.evolutionDimStart) {
            String[] currLine = line.split(";");
            int dim = Integer.parseInt(currLine[0]);
            if (currentDim == dim) {
                int phase = Integer.parseInt(currLine[1]);
                int points = Integer.parseInt(currLine[2]);
                instance.setEvolutionPhase((byte)phase, true, world, true);
                if (phase == -1)
                    instance.setTotalKills(-points, false, world, true);
                else if (phase == -2) {
                    instance.setGaining(false);
                    instance.setLoss(true);
                    return instance;
                } else
                    instance.setTotalKills(points, false, world, true);

                break;
            }
        }

        boolean dimensionFound = Arrays.stream(SRPConfigSystems.evolutionDimGain).anyMatch(v -> v==currentDim);
        if(!SRPConfigSystems.evolutionDimGainInverted == dimensionFound)
            instance.setGaining(false);

        dimensionFound = Arrays.stream(SRPConfigSystems.evolutionDimLoss).anyMatch(v -> v==currentDim);
        if (!SRPConfigSystems.evolutionDimLossInverted == dimensionFound)
            instance.setLoss(true);

        return instance;
    }

    @Override
    public SRPWorldData getByPlayer(World world, UUID playerUUID) {
        if(playerUUID!=null) {
            MapStorage storage = world.getPerWorldStorage();
            SRPWorldData instancePlayer = (SRPWorldData) storage.getOrLoadData(SRPWorldData.class, "srparasites_data" + playerUUID.toString());
            if (instancePlayer == null) {
                instancePlayer = createForPlayer(world,playerUUID, storage);
            }
            ((SRPWorldDataInterface) instancePlayer).setUUID(playerUUID);
            return instancePlayer;
        }
        //SRPMultiplier.LOGGER.info("Nischi says: getbyplayer didnt find player");
        return SRPWorldData.get(world);
    }

    @Override
    public SRPWorldData getByBlock(World world, BlockPos blockPos) {
        if(blockPos!=null) {    //TODO: are spawning mobs always at null for parasitetask?
            int x = blockPos.getX();
            int y = blockPos.getY();
            int z = blockPos.getZ();
            EntityPlayer player = getClosestPlayer(world, x, y, z, 256);

            if (player != null)
                return getByPlayer(world, player.getUniqueID());
            //else SRPMultiplier.LOGGER.info("Nischi says: getbyblock didnt find player {}", blockPos);
        } /*else
            SRPMultiplier.LOGGER.info("Nischi says: getbyblock didnt find blockpos");
        try {
            throw (new Exception("Nischi says even more"));
        } catch(Exception e){
            e.printStackTrace(System.out);
        }*/
        return SRPWorldData.get(world);
    }

    @Unique
    public EntityPlayer getClosestPlayer(World world, double x, double y, double z, double maxDist)
    {
        double minDist = -1.0D;
        EntityPlayer closestPlayer = null;

        for (EntityPlayer player: world.playerEntities){
            if (!player.isSpectator()){
                double currDistXZ = distSq(player,x,z);
                double currDist = player.getDistanceSq(x,y,z);

                if ((maxDist < 0.0D || currDistXZ < maxDist*maxDist) && (minDist == -1.0D || currDist < minDist)){
                    minDist = currDist;
                    closestPlayer = player;
                }
            }
        }

        return closestPlayer;
    }

    @Unique
    private double distSq(EntityPlayer player, double x, double z) {
        double xD = Math.abs(player.posX-x);
        double zD = Math.abs(player.posZ-z);
        return Math.max(xD,zD);
    }

    @Redirect(
            method = "checkKills",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;alertAllPlayerDim(Lnet/minecraft/world/World;Ljava/lang/String;I)V"),
            remap = false
    )
    void sendWarningToOnePlayerMixin(World worldIn, String message, int warning){
        if(SRPMultiplierConfigHandler.server.playerPhases && this.playerUUID!=null)
            PlayerPhases_AlertOnePlayer.alertOnePlayer(worldIn,this.playerUUID, message, warning);
        else
            ParasiteEventEntity.alertAllPlayerDim(worldIn, message, warning);
    }
}