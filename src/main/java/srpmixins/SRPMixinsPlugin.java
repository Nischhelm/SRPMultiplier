package srpmixins;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import srpmixins.compat.overlast.OverLastCompat;
import srpmixins.config.EarlyConfigReader;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class SRPMixinsPlugin implements IFMLLoadingPlugin {

	public SRPMixinsPlugin() {
		MixinBootstrap.init();

		//Temporary
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.scentperformance.json"); //TODO: overhaul scents and make toggles

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.weaponatkspeed.json");

		//Blacklists with no toggle
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.biomespawnblacklist.json"); //Empty whitelist doesn't really make sense here but we still won't use EarlyConfigReader for toggle mixin
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.deterrentlowphasedmg.json"); //ability for empty whitelist doesn't allow disabling

		//Has no toggle, doesn't really need one
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.infestedgrassrarity.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simmermantp.json");

		//List disables
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.minmaxphasedays.json", () -> EarlyConfigReader.isArrayFilled("Min/Max Days per Phase/Dimension",SRPMixinsConfigHandler.rules.minMaxDaysPerPhase.length != 0));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.conversionpathways.json", () -> EarlyConfigReader.isArrayFilled("Conversion Phase Lock Rules",SRPMixinsConfigHandler.spawns.conversionRules.length != 0));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.variantrules.json", () -> EarlyConfigReader.isArrayFilled("Variant Disable Rules",SRPMixinsConfigHandler.rules.variantDisableRules.length != 0));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.despawntimer.json", () -> EarlyConfigReader.isArrayFilled("Despawn Timer Rules",SRPMixinsConfigHandler.rules.despawnTimerRules.length != 0));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.blockbreakblacklist.json", () -> EarlyConfigReader.isArrayFilled("Block Break Blacklist", SRPMixinsConfigHandler.various.blockBreakBlacklist.length != 0));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.foodstealblacklist.json", () -> EarlyConfigReader.isArrayFilled("Food Steal Item Blacklist", SRPMixinsConfigHandler.various.foodBlacklist.length != 0));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.minferalisations.json", () -> EarlyConfigReader.isArrayFilled("Min Feralisations", SRPMixinsConfigHandler.coth.minFeralisations.length != 0));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.nodecompassfix.json", () -> EarlyConfigReader.isArrayFilled("Node Compass max distance", SRPMixinsConfigHandler.various.nodeCompassMaxDist.length != 0));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.summoningoverhaul.json", () -> EarlyConfigReader.isArrayFilled("Summoning Overhaul - Max Summon Points", SRPMixinsConfigHandler.spawns.summoningOverhaul.length != 0));

		//Int disables
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.adapteddespawnlock.json", () -> EarlyConfigReader.getInt("Adapted Despawn Penalty First Phase", SRPMixinsConfigHandler.phasepoints.adaptedDespawnPenaltyPhase) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.stackcollidingviralslower.json", () -> EarlyConfigReader.getInt("...", SRPMixinsConfigHandler.dmgfix.viralStackSpeedOnTouch) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.infsquidcap.json", () -> EarlyConfigReader.getInt("Water Parasite Mob Cap", SRPMixinsConfigHandler.waterparas.waterParasiteCap) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.nexuscap.json", () -> EarlyConfigReader.getInt("Nexus Mob Cap", SRPMixinsConfigHandler.deterrents.nexusCap) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.nodecolonyreset.json", () -> EarlyConfigReader.getInt("Node/Colony Removal Check Frequency", SRPMixinsConfigHandler.parabiome.nodeColonyResetCheckFrequency) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simbigspiderassimfix.json", () -> EarlyConfigReader.getInt("Sim Big Spider Min Assimilations", SRPMixinsConfigHandler.coth.assimBigSpiderMinAssimilations) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simmermancap.json", () -> EarlyConfigReader.getInt("End Simmermen Conversion Cap", SRPMixinsConfigHandler.simmermen.endSimmermenCap) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.spawninglight.json", () -> EarlyConfigReader.getInt("Min Blocklight Threshold", SRPMixinsConfigHandler.spawns.blockLightThresholdTwo) != 7);

		//Double disables
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.infsquidreach.json", () -> EarlyConfigReader.getDouble("Assimilated Squid Attack Range", SRPMixinsConfigHandler.waterparas.infSquidAttackRange) >= 0);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.fosternerf.json", () -> EarlyConfigReader.getDouble("Foster Chance", SRPMixinsConfigHandler.potions.fosterChance) >= 0);

		//Mod Compat
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.overlast.json", () -> Loader.isModLoaded("overlast"));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.overlast_customphases.json", () -> OverLastCompat.shouldEnqueueOverLastMixins(OverLastCompat.OverLastVersion.FULL));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.overlastlite_customphases.json", () -> OverLastCompat.shouldEnqueueOverLastMixins(OverLastCompat.OverLastVersion.LITE));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.deepseadanger_customphases.json", () -> Loader.isModLoaded("srpdeepseadanger") && areCustomPhasesEnabled());
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srpextra_customphases.json", () -> Loader.isModLoaded("srpextra") && areCustomPhasesEnabled());

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.bloodmoon.entirespawnfix.json", () -> Loader.isModLoaded("bloodmoon") && isFixSpawningEntirelyEnabled());

		//Incompat with my own shit - fix spawning entirely
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.mobcapmulti.json", () -> !isFixSpawningEntirelyEnabled() && EarlyConfigReader.getBoolean("Parasite Stat+Drop Multiplier: Global switch", SRPMixinsConfigHandler.dimension.doMultipliers));
		
		//Incompat with my own shit - infestation spread overhaul
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.infestationpenalty.json", () -> !isInfestationOverhaulEnabled() && EarlyConfigReader.getInt("Infestation Penalty First Phase", SRPMixinsConfigHandler.phasepoints.infestationPenaltyPhase) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.beckoninfestationlimit.json", () -> !isInfestationOverhaulEnabled() && EarlyConfigReader.getBoolean("Fix Block Infestation Limit", SRPMixinsConfigHandler.deterrents.fixInfestedBlockLimit));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.circularinfestationarea.json", () -> !isInfestationOverhaulEnabled() && EarlyConfigReader.getBoolean("Circular Infestation Area", SRPMixinsConfigHandler.deterrents.infestationAreaIsCircular));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.stopinfestedbeckondespawn.json", () -> !isInfestationOverhaulEnabled() && EarlyConfigReader.getBoolean("Stop Infested Block Beckon Despawn", SRPMixinsConfigHandler.deterrents.stopRemainBeckonDespawn));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.customphasesnoinfestationoverhaul.json", () -> !isInfestationOverhaulEnabled() && areCustomPhasesEnabled());

		//Incompat with my own shit - biome spread overhaul
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.biomespreadpenalty.json", () -> !isBiomeSpreadOverhaulEnabled() && EarlyConfigReader.getInt("Biome Spreading Penalty First Phase", SRPMixinsConfigHandler.phasepoints.biomeSpreadingPenaltyPhase) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.biomespreadlimit.json", () -> !isBiomeSpreadOverhaulEnabled() && EarlyConfigReader.getBoolean("Fix Parasitic Biome spreading limit", SRPMixinsConfigHandler.parabiome.fixBiomeSpreadingLimit));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.customphasesnobiomespreadoverhaul.json", () -> !isBiomeSpreadOverhaulEnabled() && areCustomPhasesEnabled());
	}
	
	public static boolean isBiomeSpreadOverhaulEnabled(){
		return EarlyConfigReader.getBoolean("Biome Spread Performance Overhaul", SRPMixinsConfigHandler.parabiome.biomeSpreadOverhaul);
	}

	public static boolean isInfestationOverhaulEnabled(){
		return EarlyConfigReader.getBoolean("Infestation Performance Overhaul", SRPMixinsConfigHandler.deterrents.infestationOverhaul);
	}

	public static boolean isFixSpawningEntirelyEnabled(){
		return EarlyConfigReader.getBoolean("Fix Spawning Entirely", SRPMixinsConfigHandler.spawns.fixSpawningEntirely);
	}

	public static boolean areCustomPhasesEnabled() {
		boolean playerPhases = EarlyConfigReader.getBoolean("Use Player Phases", SRPMixinsConfigHandler.playerphases.enabled);
		boolean chunkPhases = EarlyConfigReader.getBoolean("Use Chunk Phases", SRPMixinsConfigHandler.chunkphases.enabled);
		return (playerPhases || chunkPhases);
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) { }
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}
