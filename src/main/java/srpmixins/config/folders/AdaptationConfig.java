package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class AdaptationConfig {
    @Config.Comment({
            "Overhaul Living/Sentient Armor adaptation, making it more performant and fixing some issues.",
            "Fixes that are included without a toggle:",
            "- When combining living+sentient gear, will use the point multiplier of each armor piece instead of using the last checked one."
    })
    @Config.Name("Overhaul Adaptation")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(earlyMixin = "mixins.srpmixins.vanilla.adaptationoverhaul.json", lateMixin = "mixins.srpmixins.srp.adaptationoverhaul.json", defaultValue = true)
    public boolean overhaulAdaptation = true;

    @Config.Comment({
            "In SRP, adaptable Parasites will have a chance to fail adapting to a damage type if they got hit by inFire or onFire dmg maximum 10 ticks (half a second) before the current hit.",
            "This means you would have to hit them during the iframe the fire tick creates to make them fail the adaptation.",
            "Enable this fix to instead make them have a chance to fail adaptation whenever they are burning (and not having fire resistance).",
            "Warning: this makes any burn inflicting method to deal with parasites about twice as useful against their adaptation"
    })
    @Config.Name("Fix Adaptation While Burning")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.adaptwhileburningfix.json", defaultValue = true)
    public boolean fixAdaptationWhileBurning = true;

    @Config.Comment("If adapting during an attack with no immediate attacker entity, SRP adapts to \"\". This fixes that bug. Requires \"Overhaul Adaptation\".")
    @Config.Name("Fix Null Adaptation")
    public boolean fixNullAdaptation = true;

    @Config.Comment("If SRP doesn't find a blacklisted damage type for a mob/player in BlackList Mobs, it will also search the Blacklist Else list. This fixes that bug. Requires \"Overhaul Adaptation\".")
    @Config.Name("Fix Blacklist Check")
    public boolean fixBlacklistCheck = true;

    @Config.Comment("SRPConfig has a list \"Adaptation Bonus\" which isn't read properly and will crash if filled with entries. This fixes it.")
    @Config.Name("Fix Adaptation Bonus Config")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.adaptationbonusfix.json", defaultValue = true)
    public boolean fixAdaptationBonusList = true;

    @Config.Comment("Adaptable parasites will adapt to the players mainhand weapon when hit by indirect dmgs (harming splash potions, arrows, modded indirect dmg sources). Enable this to make them adapt to the indirect dmg source instead (so magic, arrow etc). Moved from RLMixins (thanks Kotlin!)")
    @Config.Name("Fix Adaptation to Indirect Damages")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.adapttoindirect.json", defaultValue = true)
    public boolean fixAdaptationToIndirect = true;

    @Config.Comment({
            "Wearing Living or Sentient Armor is supposed to apply the SRP config \"Mob Fire Damage Multiplier\" to any fire (inFire/onFire) dmg the player takes, as well as any dmg when the player is burning (isBurning). In those cases, adapting to the dmg is also supposed to fail. Fire dmg doing that didn't work in base SRP due to a bug.",
            "Use this list to modify how it works when \"Overhaul Adaptation\" is enabled.",
            "Originally intended would be inFire, onFire, isBurning, actual SRP behavior is just isBurning.",
            "Possible additions would be lava, hotFloor and fireworks, or just fully disabling the feature by clearing the list."
    })
    @Config.Name("Fire Multiplier Dmg Types")
    public String[] fireMultiDmgTypes = {
            "isBurning"
    };

    @Config.Comment({
            "How effective Living or Sentient Armor adaptations are while the player is burning (or taking fire damage types listed in \"Fire Multiplier Dmg Types\").",
            "0.0 = SRP behavior, adaptations don't work at all (damage is only increased by 4x).",
            "1.0 = Adaptations work at full effectiveness no matter the burning.",
            "0.5 = Adaptations are half as effective when burning (e.g. 80% reduction becomes 40%)."
    })
    @Config.Name("Burning Adaptation Effectiveness")
    @Config.RangeDouble(min = 0.0, max = 1.0)
    public double burningAdaptationEffectiveness = 0.5;

    @Config.Comment({
            "Damage multiplier when wearing Living/Sentient Armor and taking fire damage or being on fire.",
            "Set to 1.0 to disable extra fire damage. SRP default is 4.0.",
            "This is only used if \"Fire Multiplier Dmg Types\" is not empty."
    })
    @Config.Name("Fire Damage Multiplier Override")
    @Config.RangeDouble(min = 1.0, max = 10.0)
    public double fireDamageMultiplier = 2.0;

    @Config.Comment("SRPMixins adds a recipe to reset adaptation on Living/Sentient Armor. This needs you to surround the armor with four of the named item. To disable the recipe, clear this config and restart the game.\n" +
            "For CraftTweaker users there is also a method to use for custom recipes, using srpmixins.StackHelper.removeAdaptation(IItemStack stack);")
    @Config.Name("Adaptation Reset Item")
    public String adaptationResetItem = "contenttweaker:blood_tear";

    @Config.Comment("Optional item in the corner spots of the crafting table for the adaptation reset recipe. Keep empty for air/empty")
    @Config.Name("Adaptation Second Reset Item")
    public String adaptationResetItemTwo = "";

    @Config.Comment("Overrides what damage types should be counted as what other damage types. Use NONE to not adapt to it.")
    @Config.Name("Adaptation Type Overrides")
    public String[] adaptationTypeOverrides = {
            "srparasites:homming, thrown"
    };
}
