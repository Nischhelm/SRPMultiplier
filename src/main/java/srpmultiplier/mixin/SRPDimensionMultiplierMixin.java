package srpmultiplier.mixin;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.monster.EntityMob;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

import java.util.UUID;

@Mixin(EntityParasiteBase.class)
public abstract class SRPDimensionMultiplierMixin extends EntityMob {

    private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("554f3929-4194-4ae5-a4da-4b528a89ca32");
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("554f3929-4195-4ae5-a4da-4b528a89ca32");
    private static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("554f3929-4196-4ae5-a4da-4b528a89ca32");
    private static final UUID KBRES_MODIFIER_UUID = UUID.fromString("554f3929-4197-4ae5-a4da-4b528a89ca32");

    public SRPDimensionMultiplierMixin(World worldIn) {
        super(worldIn);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        if(SRPMultiplierConfigHandler.server.doMultipliers) {
            float multiplier;
            if (dimension == 0)
                multiplier = SRPMultiplierConfigHandler.server.overworldMultiplier;
            else if (dimension == -1)
                multiplier = SRPMultiplierConfigHandler.server.netherMultiplier;
            else if (dimension == 1)
                multiplier = SRPMultiplierConfigHandler.server.endMultiplier;
            else
                multiplier = SRPMultiplierConfigHandler.server.lcMultiplier;

            multiplier = multiplier - 1;

            if (Math.abs(multiplier) > 1e-3) {
                AttributeModifier modifierHealth = new AttributeModifier(HEALTH_MODIFIER_UUID, "SRPMultiplier Health", multiplier, 2);
                AttributeModifier modifierArmor = new AttributeModifier(ARMOR_MODIFIER_UUID, "SRPMultiplier Armor", multiplier, 2);
                AttributeModifier modifierDamage = new AttributeModifier(DAMAGE_MODIFIER_UUID, "SRPMultiplier Damage", multiplier, 2);
                AttributeModifier modifierKBResistance = new AttributeModifier(KBRES_MODIFIER_UUID, "SRPMultiplier KB Resistance", multiplier, 2);
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(modifierHealth);
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(modifierArmor);
                this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(modifierDamage);
                this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(modifierKBResistance);
            }
        }
    }
}