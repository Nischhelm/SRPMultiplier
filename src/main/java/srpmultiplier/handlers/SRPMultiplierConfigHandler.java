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

		@Config.Name("Modify Global Attribute multipliers")
		public boolean doMultipliers = true;

		@Config.Name("Overworld Parasite Multiplier")
		public float overworldMultiplier = 1.f;

		@Config.Name("Nether Parasite Multiplier")
		public float netherMultiplier = 2.f;

		@Config.Name("End Parasite Multiplier")
		public float endMultiplier = 2.f;

		@Config.Name("Lost Cities Parasite Multiplier")
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