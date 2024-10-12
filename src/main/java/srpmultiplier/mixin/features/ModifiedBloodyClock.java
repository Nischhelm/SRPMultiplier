package srpmultiplier.mixin.features;

import com.dhanantry.scapeandrunparasites.item.ItemEPClock;
import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.SRPWorldDataInterface;

import static java.lang.Math.*;

@Mixin(ItemEPClock.class)
public abstract class ModifiedBloodyClock {

    @Unique
    private SRPWorldData worldData;

    @Inject(method="func_77659_a",
            at = @At(value="HEAD"),
            remap = false
    )
    public void saveWorldDataMixin(World worldIn, EntityPlayer playerIn, EnumHand handIn, CallbackInfoReturnable<ActionResult<ItemStack>> cir){
        if(!worldIn.isRemote) {
            worldData = SRPWorldData.get(worldIn);
            if (SRPMultiplierConfigHandler.server.playerPhases)
                worldData = ((SRPWorldDataInterface) worldData).getByPlayer(worldIn, playerIn.getUniqueID());
        }
    }

    @Redirect(
            method = "func_77659_a",
            at = @At(value="INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;func_146105_b(Lnet/minecraft/util/text/ITextComponent;Z)V"),
            remap = false
    )
    public void sendPhaseMessageToChatMixin(EntityPlayer player, ITextComponent iTextComponent, boolean b) {
        if (SRPMultiplierConfigHandler.server.modifyBloodyClock) {
            byte evoPhase = worldData.getEvolutionPhase();
            int pointsNext = SRPCommandEvolution.getNeededPoints((byte) min(evoPhase + 1, 8));
            int pointsThis = SRPCommandEvolution.getNeededPoints(evoPhase);
            int perc = (int) round((100. * ((double) worldData.getTotalKills() - pointsThis)) / ((double) pointsNext - pointsThis));
            if (pointsNext == pointsThis) perc = 0;
            player.sendStatusMessage(new TextComponentString("Current Phase: "+Integer.toString(evoPhase)).appendText(" (" + perc + "%)"), true);
        } else
            player.sendStatusMessage(iTextComponent, b);
    }
}