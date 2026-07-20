package srpmixins;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfSquid;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityLum;
import com.dhanantry.scapeandrunparasites.init.SRPItems;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import srpmixins.capability.adaptation.CapabilityAdaptationHandler;
import srpmixins.capability.chunkphases.CapabilityEvoPointsHandler;
import srpmixins.compat.AntiqueAtlasCompat;
import srpmixins.compat.CompatUtil;
import srpmixins.compat.LycanitesMobsCompat;
import srpmixins.compat.crafttweaker.CT_BlockInfestationEvent;
import srpmixins.compat.hordes.HordesCompat;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.config.providers.*;
import srpmixins.handlers.*;
import srpmixins.loot.SRPPhaseLootCondition;
import srpmixins.rules.ConversionPathways;
import srpmixins.rules.ruleset.*;
import srpmixins.world.SRPWorldProvider;

@Mod(
        modid = SRPMixins.MODID,
        version = SRPMixins.VERSION,
        name = SRPMixins.NAME,
        dependencies = "required-after:fermiumbooter@[1.3.2,);required-after:srparasites@[1.9.21]",
        acceptableRemoteVersions = "*"
)
public class SRPMixins {
    public static final String MODID = "srpmixins";
    public static final String VERSION = "2.9.6.1";
    public static final String NAME = "SRPMixins";
    public static final Logger LOGGER = LogManager.getLogger();
    public static Configuration CONFIG;
    public static boolean completedLoading = false;

    public static void logInWorld(World world, String text){
        world.playerEntities.forEach(p -> p.sendMessage(new TextComponentString(text)));
        LOGGER.info("SRPMIXINS log: {}", text);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CONFIG = new Configuration(event.getSuggestedConfigurationFile());
        CONFIG.load();

        SRPMobConfigProvider.registerMobs();

        SRPMixinsConfigProvider.init();
        ConversionPathways.init();
        MobCapRuleSet.INSTANCE = new MobCapRuleSet();
        MinMaxDayPerPhaseRuleSet.INSTANCE = new MinMaxDayPerPhaseRuleSet();
        VariantDisableRuleSet.INSTANCE = new VariantDisableRuleSet();
        DespawnTimerRuleSet.INSTANCE = new DespawnTimerRuleSet();
        StatIncreaseRuleSet.INSTANCE = new StatIncreaseRuleSet();
        ChunkPhaseConfigProvider.init();
        DimensionMultiConfigProvider.init();

        if(SRPMixinsConfigHandler.chunkphases.enabled && SRPConfigSystems.useEvolution) {
            CapabilityEvoPointsHandler.registerCapability();
            MinecraftForge.EVENT_BUS.register(CapabilityEvoPointsHandler.AttachCapabilityHandler.class);
        }

        if(SRPMixinsConfigHandler.adaptation.overhaulAdaptation) {
            CapabilityAdaptationHandler.registerCapability();
            MinecraftForge.EVENT_BUS.register(CapabilityAdaptationHandler.EventHandler.class);
        }

        registerEventSubscriberIf(NexusSpawnSounds.class, SRPMixinsConfigHandler.deterrents.playsounds);
        registerEventSubscriberIf(ParasiteDropChance.class, SRPMixinsConfigHandler.dimension.doMultipliers && SRPMixinsConfigHandler.dimension.dimensionDropMultipliers.length > 0);
        registerEventSubscriberIf(CothNoDropsOnConversion.class, SRPMixinsConfigHandler.coth.fixCothOnDeath);
        registerEventSubscriberIf(SRPArmorBowEvolutionHandler.class, SRPMixinsConfigHandler.weapons.addArmorBowEvolution && !SRPMixinsConfigHandler.weapons.disableSentientEvolution);
        registerEventSubscriberIf(ConversionPathways.class, SRPMixinsConfigHandler.spawns.autoFillConversionRules);
        registerEventSubscriberIf(SpawnPotentialsHandler.class, SRPMixinsConfigHandler.spawns.fixSpawningEntirely);
        registerEventSubscriberIf(WorldMobCapHandler.class, SRPMixinsConfigHandler.spawns.fixSpawningEntirely);
        registerEventSubscriberIf(XpPerPhaseHandler.class, SRPMixinsConfigHandler.phasepoints.xpMultis.length > 0); //TODO: maybe a toggle idk
        registerEventSubscriberIf(StatIncreaseRuleHandler.class, SRPMixinsConfigHandler.rules.statIncreaseRules.length > 0);
        registerEventSubscriberIf(CamouflageHandler.class, SRPMixinsConfigHandler.coth.fixCamouflage);
        registerEventSubscriberIf(TendrilSyncHandler.class, SRPMixinsConfigHandler.various.fixTendrilRegain);
        registerEventSubscriberIf(TickSRPDataHandler.class, SRPMixinsConfigHandler.playerphases.enabled && SRPMixinsConfigHandler.playerphases.individualTicks);
        registerEventSubscriberIf(CT_BlockInfestationEvent.CT_EventForwarder.class, Loader.isModLoaded("crafttweaker"));

        LootConditionManager.registerCondition(new SRPPhaseLootCondition.Serializer());
        if(SRPMixinsConfigHandler.various.useLootTables) LootPoolProvider.setupLootPoolFolders(event.getModConfigurationDirectory());
    }

    private static void registerEventSubscriberIf(Object subscriber, boolean condition){
        if(condition) MinecraftForge.EVENT_BUS.register(subscriber);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SRPConfigProvider.init();

        if(SRPMixinsConfigHandler.sourcedim.isEnabled) {
            DimensionType.register("The Source", "_srpsource", SRPWorldProvider.DIMENSION_ID, SRPWorldProvider.class, false);
            DimensionManager.registerDimension(SRPWorldProvider.DIMENSION_ID, DimensionType.getById(SRPWorldProvider.DIMENSION_ID));
        }

        //These only run once on the first startup when ppl enabled more phases or mob config
        if(SRPMixinsConfigHandler.morephases.enableMorePhases && SRPMixinsConfigHandler.morephases.phaseKills.length == 0)
            MorePhasesConfigProvider.initMorePhasesConfig();
        if (SRPMixinsConfigHandler.mobConfig.enableMobConfig && SRPMixinsConfigHandler.mobConfig.mobConfig.length == 0)
            SRPMobConfigProvider.initMobConfigs();

        if(SRPMixinsConfigHandler.morescents.enableMoreScents) {
            if(SRPMixinsConfigHandler.morescents.pointsRequired.length == 0)
                //This only runs once on the first startup when ppl enabled more scents
                MoreScentsConfigProvider.initMoreScentsConfig();
            MoreScentsConfigProvider.init();
        }

        if(Loader.isModLoaded("antiqueatlas") && SRPMixinsConfigHandler.modcompat.enableAntiqueAtlasCompat && event.getSide() == Side.CLIENT)
            AntiqueAtlasCompat.initTiles();

        if(Loader.isModLoaded("hordes") && SRPMixinsConfigHandler.modcompat.enableHordesCompat) HordesCompat.init();

        if(SRPMixinsConfigHandler.weapons.repairableGear) {
            SRPItems.MATERIAL_LIVING.setRepairItem(new ItemStack(SRPItems.infblade));
            SRPItems.ARMOR_LIVING.setRepairItem(new ItemStack(SRPItems.vileshell));
            SRPItems.ARMOR_SENTIENT.setRepairItem(new ItemStack(SRPItems.vileshell));
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if(CompatUtil.lycanitesmobs.isLoaded())
            LycanitesMobsCompat.reloadLycaniteSpawnerManager();

        SRPConfigProvider.postInit();
        if(SRPMixinsConfigHandler.various.useLootTables) LootPoolProvider.getLootPoolsFromConfigOrFile();
        MorePhasesConfigProvider.postInit();

        completedLoading = true;

        if(SRPMixinsConfigHandler.waterparas.enableWaterSpawns) {
            EntitySpawnPlacementRegistry.setPlacementType(EntityInfSquid.class, EntityLiving.SpawnPlacementType.IN_WATER);
            EntitySpawnPlacementRegistry.setPlacementType(EntityLum.class, EntityLiving.SpawnPlacementType.IN_WATER);
        }
    }
}
