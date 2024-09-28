package srpmultiplier;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import srpmultiplier.handlers.SRPSpawningHandler;

@Mod(modid = SRPMultiplier.MODID, version = SRPMultiplier.VERSION, name = SRPMultiplier.NAME, dependencies = "required-after:fermiumbooter")
public class SRPMultiplier {
    public static final String MODID = "srpmultiplier";
    public static final String VERSION = "1.0.2";
    public static final String NAME = "SRPMultiplier";
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(SRPSpawningHandler.class);
    }
}