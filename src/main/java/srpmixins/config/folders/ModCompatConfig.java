package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class ModCompatConfig {
    @Config.Comment("Set to false to disable all bloodmoon related tweaks")
    @Config.Name("Enable Bloodmoon tweaks")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.bloodmoon.json", defaultValue = false)
    @MixinConfig.CompatHandling(modid = "bloodmoon", desired = true, reason = "Mod Compat for Bloodmoon, requires Bloodmoon", warnIngame = false)
    @MixinConfig.CompatHandling(modid = "lostcities", desired = true, reason = "Mod Compat for Lost Cities, requires Lost Cities", warnIngame = false)
    @Config.RequiresMcRestart
    public boolean enableBloodmoon = false;

    @Config.Comment("Set to false to disable all lost cities related tweaks. This also disables the bloodmoon tweaks as they only work inside the lost cities")
    @Config.Name("Enable Lost Cities tweaks")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.lostcities.json", defaultValue = false)
    @MixinConfig.CompatHandling(
            modid = "lostcities",
            desired = true,
            reason = "Mod Compat for Lost Cities, requires Lost Cities",
            warnIngame = false
    )
    @Config.RequiresMcRestart
    public boolean enableLostCities = false;

    @Config.Comment("Disable Lures in LC and instead spawn a Dispatcher Nidus")
    @Config.Name("Lures disabled in LC")
    public boolean disableLuresInLC = true;

    @Config.Comment("Blood moons happen in Lost Cities dimension (requires this mod on client to see red moon), with increased parasite mob cap")
    @Config.Name("Do Blood Moons in LC")
    public boolean bloodmoonInLC = true;

    @Config.Comment("Multiply Parasite Mob Cap by this much during Blood Moons (if using SRP custom spawner). \n" +
            "Doesn't work if \"Fix Spawning Entirely\" is enabled, use \"Parasite Mob Cap Rules\" for that")
    @Config.Name("Bloodmoon Parasite Cap Multiplier")
    @Config.RangeInt(min = 0)
    public float bloodmoonInLCmobCapMultiplier = 4;

    @Config.Comment("LC Portals are locked until reaching this phase. Disable with -1")
    @Config.Name("LC Portal Phase Lock")
    public int portalLClockedPhase = 6;

    @Config.Comment("SRPMixins has compat with OverLast when using custom phases (player phases or chunk phases). \n" +
            "Use this to toggle off the compatibility.\n" +
            "It is auto disabled anyway if you dont use custom phases or OverLast.\n" +
            "Works with both OverLast and OverLastLite (only HUD)")
    @Config.Name("Enable OverLast custom phases")
    @Config.RequiresMcRestart
    public boolean enableOverLastCustomPhases = true;

    @Config.Comment("Remove point display from overlast phase HUD for more immersion and uncertainty.")
    @Config.Name("OverLast HUD Without Points")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.overlast.nopointsinhud.json", defaultValue = false)
    @MixinConfig.CompatHandling(
            modid = "overlast",
            desired = true,
            reason = "Mod Compat for OverLast, requires OverLast"
    )
    @Config.RequiresMcRestart
    public boolean overlastHUDnoPoints = false;

    @Config.Comment("Fixes the OpenGL State before rendering the OverLast evolution bar so it doesn't take on the color of the gui that was rendered before it.")
    @Config.Name("OverLast HUD GL State Fix")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.overlast.glstatefix.json", defaultValue = false)
    @MixinConfig.CompatHandling(
            modid = "overlast",
            desired = true,
            reason = "Mod Compat for OverLast, requires OverLast",
            warnIngame = false
    )
    @Config.RequiresMcRestart
    public boolean overlastGLFix = true;

    @Config.Comment("Set to true to make \"Scape and Spartan: Parasites\" weapons with the Uncapped property ignore parasite damage caps.\n" +
            "NOTE: This property works without SRPMixins as well, this just streamlines the handling a bit. \n" +
            "I kinda thought it didn't work - without actually testing it...")
    @Config.Name("Scape and Spartan: Parasites Compat - Ignore Dmg Cap")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.swparasites.json", defaultValue = true)
    @MixinConfig.CompatHandling(
            modid = "swparasites",
            desired = true,
            reason = "Mod Compat for Scape and Spartan: Parasites, requires Scape and Spartan: Parasites",
            warnIngame = false
    )
    @Config.RequiresMcRestart
    public boolean enableSWParasitesCompat = true;

    @Config.Comment("This allows using some additional rules in InControl specifically for SRP\n" +
            "New Rules: \"srp_minphase\", \"srp_maxphase\", \"srp_minnodes\", \"srp_maxnodes\", \"srp_mincolos\", \"srp_maxcolos\"\n" +
            "All rules take a single integer number as comparison and can be used in any incontrol json.\n" +
            "Note: Nodes and Colonies are counted over all dimensions, thats just how SRP works")
    @Config.Name("In Control Compat - Add Rules")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.incontrol.json", defaultValue = true)
    @MixinConfig.CompatHandling(
            modid = "incontrol",
            desired = true,
            reason = "Mod Compat for In Control, requires In Control",
            warnIngame = false
    )
    @Config.RequiresMcRestart
    public boolean enableInControlCompat = true;

    @Config.Comment("Renders custom tiles in the Antique Atlas for parasite biomes.")
    @Config.Name("Antique Atlas Compat - Render Biome Tile")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.antiqueatlas.renderbiometile.json", defaultValue = true)
    @MixinConfig.CompatHandling(
            modid = "antiqueatlas",
            desired = true,
            reason = "Mod Compat for Antique Atlas, requires Antique Atlas",
            warnIngame = false
    )
    @Config.RequiresMcRestart
    public boolean enableAntiqueAtlasCompat = true;

    @Config.Comment("Registers \"srparasites:phase\", \"srparasites:kills\" and \"srparasites:nodes\" value getters and more importantly  \"srpmixins:phase\", \"srpmixins:kills\" and \"srpmixins:nodes\" conditions for The Hordes mod.\n" +
            "Note: i prefixed the conditions with srpmixins modid to not compete with Hordes unreleased SRP compat conditions srparasites:phase and srparasites:kills which work slightly differently and don't have srpmixins custom phase compat.")
    @Config.Name("Hordes Compat - Register Conditions & ValueGetters")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.hordes.json", defaultValue = true)
    @MixinConfig.CompatHandling(
            modid = "hordes",
            desired = true,
            reason = "Mod Compat for The Hordes, requires The Hordes",
            warnIngame = false
    )
    @Config.RequiresMcRestart
    public boolean enableHordesCompat = true;

    @Config.Comment({
            "The issue here is Aqua Remains using a clientside field crashing them.",
            "This is caught but makes them not spawn, and spams the server log",
            "For SRPExtra 0.6.x, will probably crash in other versions"
    })
    @Config.Name("SRPExtra Compat - Fix serverside damageAni issues")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srpextra_damageanifix.json", defaultValue = false)
    @MixinConfig.CompatHandling(
            modid = "srpextra",
            desired = true,
            reason = "Mod Compat for SRPExtra, requires SRPExtra",
            warnIngame = false
            //, targetVersionRange = "[0.6.2]"
    )
    @Config.RequiresMcRestart
    public boolean enableSRPExtraFixes = false;

    @Config.Comment("Registers a Simple Voice Chat Plugin that makes parasites hear players.")
    @Config.Name("Simple Voice Chat Compat")
    @Config.RequiresMcRestart
    public boolean enableVoiceChatCompat = true;

    @Config.Comment("If Sound Volume (in db) is higher than this, parasites will hear players")
    @Config.Name("Simple Voice Chat Compat - Volume Threshold")
    @Config.RangeDouble(min = -100, max = 0)
    @Config.SlidingOption
    public double voiceChatThreshold = -10.;

    @Config.Comment("Parasites farther away than this distance (in blocks) will never hear players")
    @Config.Name("Simple Voice Chat Compat - Max Distance")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 100)
    @Config.SlidingOption
    public int voiceChatMaxDistance = 50;

    @Config.Comment({
            "If enabled, will write a status message \"They can hear you\" when parasites hear the player through Simple Voice Chat. ",
            "WIP, i kinda want this to be less intrusive"
    })
    @Config.Name("Simple Voice Chat Compat - Notify Player")
    public boolean voiceChatNotify = true;
}
