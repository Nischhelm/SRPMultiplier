package srpmultiplier.mixin.features;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

import static com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure.VARIANT;

@Mixin(BlockEvolutionLure.class)
public abstract class LureValuePerPhase {

    @Unique
    IBlockState state;

    @Inject(
            method="func_180639_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;setTotalKills(IZLnet/minecraft/world/World;Z)Z"),
            remap=false
    )
    void saveStateMixin(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir){
        this.state = state;
    }

    @Redirect(
            method="func_180639_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;setTotalKills(IZLnet/minecraft/world/World;Z)Z"),
            remap=false
    )
    public boolean lurePhaseMultiplierMixin(SRPWorldData data, int in, boolean plus, World worldIn, boolean canChangePhase){
        if(SRPMultiplierConfigHandler.server.variableLureValues && SRPMultiplierConfigHandler.server.lurePhaseMultis.length==9) {
            int lureValue = 0;
            byte evoPhase = data.getEvolutionPhase();
            switch (state.getValue(VARIANT)){
                case ONE:   lureValue = SRPConfigSystems.luredValueOne; break;
                case TWO:   lureValue = SRPConfigSystems.luredValueTwo; break;
                case THREE: lureValue = SRPConfigSystems.luredValueThree; break;
                case FOUR:  lureValue = SRPConfigSystems.luredValueFour; break;
                case FIVE:  lureValue = SRPConfigSystems.luredValueFive; break;
                case SIX:   lureValue = SRPConfigSystems.luredValueSix; break;
                case SEVEN: lureValue = SRPConfigSystems.luredValueSeven; break;
                case EIGHT: lureValue = SRPConfigSystems.luredValueEight; break;
            }
            int multi = 0;
            switch (evoPhase){
                case 0: multi = SRPMultiplierConfigHandler.server.lurePhaseMultis[0]; break;
                case 1: multi = SRPMultiplierConfigHandler.server.lurePhaseMultis[1]; break;
                case 2: multi = SRPMultiplierConfigHandler.server.lurePhaseMultis[2]; break;
                case 3: multi = SRPMultiplierConfigHandler.server.lurePhaseMultis[3]; break;
                case 4: multi = SRPMultiplierConfigHandler.server.lurePhaseMultis[4]; break;
                case 5: multi = SRPMultiplierConfigHandler.server.lurePhaseMultis[5]; break;
                case 6: multi = SRPMultiplierConfigHandler.server.lurePhaseMultis[6]; break;
                case 7: multi = SRPMultiplierConfigHandler.server.lurePhaseMultis[7]; break;
                case 8: multi = SRPMultiplierConfigHandler.server.lurePhaseMultis[8]; break;
            }

            data.setTotalKills(-lureValue*multi,true,worldIn,true);
        }
        return data.setTotalKills(in, plus, worldIn, canChangePhase);
    }
}