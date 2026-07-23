package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class PotionConfig {
    @Config.Comment("SRP soft crashes whenever Needler tries to apply dmg to players. This is fixed if \"Needler Fix\" is enabled. Enable this config to finally make players suffer the fixed Needler effect.")
    @Config.Name("Needler Fix - Allow on Players")
    public boolean allowPlayerNeedler = false;

    @Config.Comment("SRP always incorrectly applies max dmg ( on mobs getting Needler, no matter the potion effect lvl. It also never applies it on players. This fixes both.")
    @Config.Name("Needler Fix")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.needlerfix.json", defaultValue = true)
    public boolean fixNeedler = true;

    @Config.Comment("SRP provides a configable maximum dmg for Needler applied on players, but the base percentage of max health which Needler does as dmg is the same for mobs and players. Use this value to customise this for players. Use any negative value to copy from SRPSystems value \"Needler Damage\" (default 0.4=40%)\n" +
            "Warning: Needler uses a fully custom damaging system via setHealth, which will ignore all other mods attempting to reduce/ignore dmg or cancel attacks/deaths. Only totems will protect players here.")
    @Config.Name("Needler Fix - Dmg Multi for Players")
    public float playerNeedlerMulti = 0.4F;

    @Config.Comment("Potions should always be applied serverside, otherwise there can be desyncs. This fixes a few spots where SRP applies potions on clientside")
    @Config.Name("Fix clientside potions")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.clientpotions.json", defaultValue = true)
    public boolean fixClientPotions = true;

    @Config.Comment("SRP Potions Rage and Heightened Senses use random UUIDs, making them stack on every restart. This fixes it.")
    @Config.Name("Fix attribute potions")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.potionuuidfix.json", defaultValue = true)
    public boolean fixPotionUUIDs = true;

    @Config.Comment({
            "Base SRP Foster effect (only given by Colony Carriers) is op.",
            "It makes adaptable parasites increase their resistances by one step about once every second (more exactly: 25 ticks)",
            "That might feel a bit much. You can set a chance here for this to succeed.",
            "Set to a negative value to disable the mixin."
    })
    @Config.Name("Foster Chance")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = -0.01, max = 1)
    public float fosterChance = 0.02F;

    @Config.Comment({
            "Base SRP Fear has a modifiable damage multiplier for entities in the air (!onGround) or flying (capabilities.isFlying).",
            "Enable this toggle to make the damage multiplier only apply when actually flying, not when just jumping or falling."
    })
    @Config.Name("Fear Damage Only When Flying")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.feardmgcondition.json", defaultValue = false)
    public boolean fearDmgOnlyWhenFlying = false;
}
