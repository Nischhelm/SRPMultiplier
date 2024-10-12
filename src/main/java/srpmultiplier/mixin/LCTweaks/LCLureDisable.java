package srpmultiplier.mixin.LCTweaks;

import com.dhanantry.scapeandrunparasites.block.BlockBase;
import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(BlockEvolutionLure.class)
public abstract class LCLureDisable extends BlockBase {

    public LCLureDisable(Material material, String name, float hardness, boolean creative, boolean tickRandom) {
        super(material, name, hardness, creative, tickRandom);
    }

    public LCLureDisable(Material material, String name, float hardness, boolean creative, boolean tickRandom, float resistance) {
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
        }
    }
}