package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponMeleeMaul;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(WeaponMeleeMaul.class)
public abstract class SentientMaulLangKeyFix extends WeaponToolMeleeBase {
    public SentientMaulLangKeyFix(ToolMaterial material, String name, double attackspeed, float range, float attackD, boolean fear, byte id) {
        super(material, name, attackspeed, range, attackD, fear, id);
    }

    @ModifyArg(
            method = "addInformation",
            at = @At(value = "INVOKE", target = "Ljava/lang/StringBuilder;append(I)Ljava/lang/StringBuilder;")
    )
    private int srpmixins_modifyLangkeys(int i) {
        if(i == 10) return 69;
        if(i == 100) return 169;
        return i;
    }
}
