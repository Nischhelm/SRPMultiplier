package srpmultiplier.mixin;

import com.dhanantry.scapeandrunparasites.block.BlockBase;
import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
public abstract class BlockEvolutionLureMixin extends BlockBase {

    public BlockEvolutionLureMixin(Material material, String name, float hardness, boolean creative, boolean tickRandom) {
        super(material, name, hardness, creative, tickRandom);
    }

    public BlockEvolutionLureMixin(Material material, String name, float hardness, boolean creative, boolean tickRandom, float resistance) {
        super(material, name, hardness, creative, tickRandom, resistance);
    }

    @Unique
    IBlockState state;

    @Inject(
            method="func_180639_a",
            at=@At(value="HEAD"),
            remap=false,
            cancellable = true
    )
    public void lureDisableMixin(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir){
        if(SRPMultiplierConfigHandler.server.disableLuresInLC && !worldIn.isRemote) {
            int dimension = playerIn.dimension;
            ItemStack head = new ItemStack(playerIn.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem());
            if (head.getItem() == Items.AIR & dimension == 111 & SRPConfigSystems.useEvolution) {
                playerIn.sendStatusMessage(new TextComponentString("The hive whispers to you"), true);
                worldIn.setBlockState(pos, SRPBlocks.dodN.getDefaultState());
                cir.setReturnValue(super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ));
            }
        } else {
            this.state = state;
        }
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