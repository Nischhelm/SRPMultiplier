package srpmultiplier.mixin.simenderman;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.*;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.SRPWorldDataInterface;

import java.util.Collections;
import java.util.List;

@Mixin(EntityInfEnderman.class)
public abstract class SimEndermanTp extends EntityPInfected {

    public SimEndermanTp(World worldIn) {
        super(worldIn);
    }

    @Shadow(remap = false)
    private int ally;

    @Shadow(remap = false)
    private int toTeleCool;

    @Shadow(remap = false)
    private int spotCool;

    @Shadow(remap = false)
    private EntityParasiteBase toTele;

    @Shadow(remap = false)
    protected abstract boolean teleportToEntity(Entity in, double dis);

    @Shadow(remap = false)
    protected abstract void setCoordTarget(double x, double y, double z);

    /**
     * @author Nischhelm
     * @reason Simmerman should tp other mobs than Assimilated
     */
    @Overwrite(remap = false)
    protected boolean teleportAllies() {
        if (this.ally <= 0 && SRPConfigMobs.infendermanteleally && this.toTeleCool <= 0 && this.spotCool <= 0) {
            EntityLivingBase target = this.getAttackTarget();
            if (target != null) {
                AxisAlignedBB axisalignedbb = (new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX + 1.0, this.posY + 1.0, this.posZ + 1.0)).grow(SRPMultiplierConfigHandler.server.simmermenTpDistance);
                List<EntityParasiteBase> moblist = this.world.getEntitiesWithinAABB(EntityParasiteBase.class, axisalignedbb);
                Collections.shuffle(moblist);

                for (EntityParasiteBase mob : moblist)
                    if (mob != this && mob.getAttackTarget() == null) {

                        byte evophase;
                        SRPWorldData data = SRPWorldData.get(world);
                        if (SRPMultiplierConfigHandler.server.playerPhases)
                            evophase = ((SRPWorldDataInterface) data).getByBlock(world, getPosition()).getEvolutionPhase();
                        else
                            evophase = data.getEvolutionPhase();

                        byte phasePrimTP = SRPMultiplierConfigHandler.server.simmermenTpPrimPhase;
                        byte phaseAdaTP = SRPMultiplierConfigHandler.server.simmermenTpAdaPhase;
                        byte phasePureTP = SRPMultiplierConfigHandler.server.simmermenTpPurePhase;

                        boolean correctType = mob instanceof EntityPInfected;
                        if(SRPMultiplierConfigHandler.server.simmermenTpMoreMobs)
                            correctType = correctType ||
                                    (mob instanceof EntityPPrimitive && evophase >= phasePrimTP) ||
                                    (mob instanceof EntityPAdapted && evophase >= phaseAdaTP) ||
                                    (mob instanceof EntityPPure && evophase >= phasePureTP);
                        if (correctType)
                            if (!(mob instanceof EntityCanMelt && ((EntityCanMelt) mob).isMelting()))
                                if (mob.getHealth() - SRPConfigMobs.infendermanTeleDamage >= 2.0F && this.teleportToEntity(mob, 1.0)) {
                                    this.setCoordTarget(target.posX, target.posY, target.posZ);
                                    this.toTele = mob;
                                    this.setWorkTask(false);
                                    this.ally = 1;
                                    return true;
                                }
                    }
            }
        }
        return false;
    }
}