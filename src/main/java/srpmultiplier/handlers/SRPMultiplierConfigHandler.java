package srpmultiplier.handlers;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmultiplier.SRPMultiplier;

@Config(modid = SRPMultiplier.MODID)
public class SRPMultiplierConfigHandler {
	
	@Config.Comment("Server-Side Options")
	@Config.Name("Server Options")
	public static final ServerConfig server = new ServerConfig();

/*	@Config.Comment("Client-Side Options")
	@Config.Name("Client Options")
	public static final ClientConfig client = new ClientConfig();*/

	public static class ServerConfig {

		@Config.Name("Parasite Stat Multiplier: Global switch")
		public boolean doMultipliers = true;

		@Config.Name("Parasite Stat Multiplier Overworld ")
		public float overworldMultiplier = 1.f;

		@Config.Name("Parasite Stat Multiplier Nether")
		public float netherMultiplier = 2.f;

		@Config.Name("Parasite Stat Multiplier End")
		public float endMultiplier = 2.f;

		@Config.Name("Parasite Stat Multiplier Lost Cities")
		public float lcMultiplier = 4.f;

		@Config.Comment("Disable Lures in LC and instead spawn a Dispatcher Nidus")
		@Config.Name("Lures disabled in LC")
		public boolean disableLuresInLC = true;

		@Config.Comment("Enable Parasite Spawners")
		@Config.Name("Parasite MobSpawners enabled")
		public boolean enableSpawners = true;

		@Config.Comment("Bloody Clock also displays progress to next phase in percent")
		@Config.Name("Bloody Clock percentage")
		public boolean modifyBloodyClock = true;

		@Config.Comment("Play respective sounds when Beckons or Dispatchers of higher stages naturally spawn")
		@Config.Name("Play Sounds")
		public boolean playsounds = true;

		@Config.Name("Strange Bones stack to 16")
		public boolean strangeBonesStack = true;

		@Config.Name("Do Blood Moons in LC")
		public boolean bloodmoonInLC = true;

		@Config.Comment("Multiply Parasite Mob Cap by this much during Blood Moons (if using SRP custom spawner)")
		@Config.Name("Bloodmoon Parasite Cap Multiplier")
		public int bloodmoonInLCmobCapMultiplier = 4;

		@Config.Comment("Multiply stats of parasites per evolution phase by this much, stats x (1 + phaseMultiplier x phase)")
		@Config.Name("Stat Multiplier per Evolution Phase")
		public double phaseMultiplier = 0.0;

		@Config.Comment("Allow Assimilated Endermen to tp Primitive and Adapted mobs as well")
		@Config.Name("Assimilated Endermen tp more Mobs")
		public boolean simmermenTpMoreMobs = true;

		@Config.Comment("Phase from which Assimilated Endermen teleport primitive Parasites")
		@Config.Name("Assimilated Endermen tp primitive Parasites phase")
		public byte simmermenTpPrimPhase = 4;

		@Config.Comment("Phase from which Assimilated Endermen teleport primitive Parasites")
		@Config.Name("Assimilated Endermen tp adapted Parasites phase")
		public byte simmermenTpAdaPhase = 5;

		@Config.Comment("Phase from which Assimilated Endermen teleport primitive Parasites")
		@Config.Name("Assimilated Endermen tp pure Parasites phase")
		public byte simmermenTpPurePhase = 6;
		@Config.Comment("Distance from which Assimilated Endermen search for mobs to tp")
		@Config.Name("Assimilated Endermen tp radius")
		public double simmermenTpDistance = 40.0;

		@Config.Comment("LC Portals are locked until reaching this phase. Disable with -1")
		@Config.Name("LC Portal Phase Lock")
		public int portalLClockedPhase = 6;

		@Config.Comment("Custom Mob Cap for Nexus Parasites (Dispatcher+Beckon) using SRP Phase Custom Spawner. Nexus Parasites still count to the global SRP Mob Cap. Disable with -1")
		@Config.Name("Nexus Mob Cap")
		public int nexusCap = 15;

		@Config.Comment("Stop mobs from spawning in lazy chunks by failing the spawn attempt. Slows spawning in low RD (<9)")
		@Config.Name("No spawns in lazy chunks - slow")
		public boolean noLazySpawnsSlow = true;

		@Config.Comment("Stop mobs from spawning in lazy chunks by not considering lazy loaded chunks. Results in seemingly fast spawns at low RD (<9). Ignores the slow version if this is set to true")
		@Config.Name("No spawns in lazy chunks - fast")
		public boolean noLazySpawnsFast = false;

		@Config.Comment("Make living+sentient armor also limit Fear and Viral lvls that are applied during an attack")
		@Config.Name("Fix Sentient Armor Cure")
		public boolean fixSentientArmorCuring = true;

		@Config.Comment("Whitelist Deterrent and Nexus mobs to take dmg per second if world is in low evolution phase")
		@Config.Name("Deterrents take damage from low phase whitelist ")
		public String[] whiteListedDeterrents = {"srparasites:kyphosis","srparasites:sentry","srparasites:seizer","srparasites:dispatcherten","srparasites:beckon_si","srparasites:beckon_sii","srparasites:beckon_siii","srparasites:beckon_siv","srparasites:dispatcher_si","srparasites:dispatcher_sii","srparasites:dispatcher_siii","srparasites:dispatcher_siv"};

		@Config.Comment("Set to true to use Deterrent taking dmg whitelist as blacklist")
		@Config.Name("Deterrent whitelist is blacklist")
		public boolean blackListDeterrents = false;

		@Config.Comment("Only give one evolution phase point penalty when players sleep instead of a penalty per sleeping player")
		@Config.Name("Flat sleep point penalty")
		public boolean flatSleepPenalty = true;

		@Config.Comment("Make Assimilated Endermen be able to despawn if they got converted in the end")
		@Config.Name("End Simmermen despawn")
		public boolean despawnEndSimmermen = true;

		@Config.Comment("Max amount of Assimilated Endermen that can spawn via assimilation in the end (Disable with -1)")
		@Config.Name("End Simmermen Conversion Cap")
		public int endSimmermenCap = 40;

		@Config.Comment("Change Lure Point Reduction based on Phase")
		@Config.Name("Phase dependent Lure Values")
		public boolean variableLureValues = true;

		@Config.Comment("Phase multiplier on lure values (0 to 8)")
		@Config.Name("Lure Phase Multipliers")
		public int[] lurePhaseMultis = {10,10,15,300,3000,50000,50000,100000,100000};

		@Config.Comment("Do Phase+Point functionalities per player, allowing better Multiplayer")
		@Config.Name("Use Player Phases")
		public boolean playerPhases = true;
    }

	/*public static class ClientConfig {

		@Config.Comment("Example client side config option")
		@Config.Name("Example Client Option")
		public boolean exampleClientOption = true;
	}*/

	@Mod.EventBusSubscriber(modid = SRPMultiplier.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(SRPMultiplier.MODID)) {
				ConfigManager.sync(SRPMultiplier.MODID, Config.Type.INSTANCE);
			}
		}
	}
}