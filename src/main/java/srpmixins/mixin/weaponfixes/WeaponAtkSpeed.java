package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import com.llamalad7.mixinextras.sugar.Local;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.Arrays;
import java.util.List;


@Mixin(WeaponToolMeleeBase.class)
public abstract class WeaponAtkSpeed {
    @Unique private static final List<String> SRPMIXINS$WEAPONNAMES = Arrays.asList( //this is by their byte id given in <init>
        "Living Scythe", //id 1, idx 0
        "Sentient Scythe",
        "Living Axe",
        "Sentient Axe",
        "Living Sword",
        "Sentient Sword",
        "Living Cleaver",
        "Sentient Cleaver",
        "Living Maul",
        "Sentient Maul",
        "Living Lance",
        "Sentient Lance" //id 12, idx 11
    );
    
    @ModifyVariable(
            method = "<init>",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/item/tool/WeaponToolMeleeBase;attackSpeed:D", opcode = Opcodes.PUTFIELD),
            name = "attackspeed",
            remap = false
    )
    private static double srpmixins_modifyAtkSpeed(double origAttackSpeed, @Local(argsOnly = true) byte id) {
        String weaponName = SRPMIXINS$WEAPONNAMES.get(id-1);
        if(!SRPMixinsConfigHandler.weapons.attackSpeeds.containsKey(weaponName))
            return origAttackSpeed;
        else
            return SRPMixinsConfigHandler.weapons.attackSpeeds.get(weaponName) - 4.0D;
    }
}
