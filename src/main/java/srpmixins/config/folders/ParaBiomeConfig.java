package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class ParaBiomeConfig {

    @Config.Comment("Fully overhauls the parasite biome spread logic to make it more performant. If there are other mods that use mixins with the biome spread system, there will be incompatibilities.\n" +
            "Includes a fix for ore blocks turning into Pestilent Ore Blocks.\n" +
            "Incompatible with Cotesia Glomerata")
    @Config.Name("Biome Spread Performance Overhaul")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.biomespreadoverhaul.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "srpcotesia", desired = false, reason = "Disable \"Biome Spread Performance Overhaul\"!", warnIngame = false)
    public boolean biomeSpreadOverhaul = true;

    @Config.Comment("SRParasites.cfg has two options for para biome spreading speed (cooldown+block limit), but those don't get applied. Set to true to fix that")
    @Config.Name("Fix Parasitic Biome spreading limit")
    public boolean fixBiomeSpreadingLimit = false;

    @Config.Comment("SRP keeps a single fog density value for all players at the same time. " +
            "In Multiplayer this leads to desyncs with no fog appearing inside the biome, or fog appearing outside of the biome. " +
            "This fixes it.")
    @Config.Name("Fix Parasitic Biome Fog")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.fogmultiplayerfix.json", defaultValue = true)
    public boolean fixBiomeFog = true;

    @Config.Comment("Smooths client fog color and density updates and prevents unrelated world loads from clearing rendered fog. Uses a linear fog range instead of SRP's exponential curve. Disable and restart to restore original client fog rendering; the multiplayer fog fix is independent.")
    @Config.Name("Smooth Parasitic Biome Fog")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.smoothfog.json", defaultValue = true)
    public boolean smoothBiomeFog = true;

    @Config.Comment("Parasite Bush and Vines will force load chunks when a parasite biome is growing. This stops the force loading. Moved from RLMixins (thanks fonny!)")
    @Config.Name("Fix Parasite Bush Generation Lag")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.parasitebushgen.json", defaultValue = true)
    public boolean fixBiomeBushLag = true;

    @Config.Comment("SRP sends a new network packet for every individual block position that gets turned into parasitic biome or back to plains. This fix sends one bigger packet instead, for performance.")
    @Config.Name("Fix SRP Biome Packet")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.biomepacket.json", defaultValue = true)
    public boolean fixBiomePacket = true;

    @Config.Comment("Makes some values of the Biome Purifier tweakable (see configs here) and makes it more performant.\n" +
            "Included fixes without a toggle:\n" +
            "\t- Applies potion effects (glowing/rage) in the whole chunk column (down to bedrock) instead of just in 32 blocks cube distance\n" +
            "\t- Reverts infestation on block place instead of on random tick\n" +
            "\t- Applies potion effects on block interact instead of on random tick")
    @Config.Name("Biome Purifier - Tweak+Fix")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.biomepurifier.json", defaultValue = true)
    public boolean fixBiomePurifier = true;

    @Config.Comment("How many blocks away the biome purifier will clean up infested (not parasite biome!) blocks when placed.")
    @Config.Name("Biome Purifier - Infestation Reversion Range")
    @Config.RangeInt(min = 0)
    public int biomePurifReversionRange = 5;

    @Config.Comment("Base SRP Biome Purifier only applies glowing to the first found Nexus parasite, this fix applies it to all of them")
    @Config.Name("Biome Purifier - Apply Glowing To All")
    public boolean biomePurifApplyGlowingToAll = true;

    @Config.Comment("How many ticks a Nexus mob will have Glowing for after being affected by a biome purifier")
    @Config.Name("Biome Purifier - Glowing Duration")
    @Config.RangeInt(min = 0)
    public int biomePurifGlowingDuration = 1200;

    @Config.Comment("How many ticks parasites in range will have Rage II for after being affected by a biome purifier")
    @Config.Name("Biome Purifier - Rage Duration")
    @Config.RangeInt(min = 0)
    public int biomePurifRageDuration = 1200;

    @Config.Comment("How many chunks(!) away entities are affected (glowing/rage) by the biome purifier.")
    @Config.Name("Biome Purifier - Effect range")
    @Config.RangeInt(min = 0)
    public int biomePurifPotionRange = 2;

    @Config.Comment("How often (in ticks) the server checks whether nodes/colonies have been removed (default: 24000). Set to -1 to disable the mixin.")
    @Config.Name("Node/Colony Removal Check Frequency")
    @Config.RangeInt(min = -1)
    public int nodeColonyResetCheckFrequency = 200;
}
