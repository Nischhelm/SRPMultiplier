package srpmultiplier.handlers;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.network.SRPPacketMovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class ParasiteEventEntityAddition {

    public static void alertOnePlayer(World worldIn, UUID playerUUID, String message, int warning) {
        List<EntityPlayer> playerEntityList = worldIn.playerEntities;

        EntityPlayer nearestPlayer = null;

        for (EntityPlayer player : playerEntityList) {
            if (player.getUniqueID() == playerUUID) {
                nearestPlayer = player;
                player.sendMessage(new TextComponentString(message));
                SRPMain.network.sendTo(new SRPPacketMovingSound(warning),(EntityPlayerMP) worldIn.getPlayerEntityByUUID(playerUUID));
                break;
            }
        }

        if (nearestPlayer!= null && warning == -7 && message.equals("Phase decreased"))
            for (Entity entity : worldIn.loadedEntityList)
                if (entity instanceof EntityParasiteBase && entity.getDistanceSq(nearestPlayer)<=256*256)
                    ((EntityParasiteBase) entity).addPotionEffect(new PotionEffect(SRPPotions.RAGE_E, 2400, 1, false, false));
    }
}