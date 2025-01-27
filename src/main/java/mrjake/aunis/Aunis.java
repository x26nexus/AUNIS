package mrjake.aunis;

import org.apache.logging.log4j.Logger;

import mrjake.aunis.command.CommandQueryStargate;
import mrjake.aunis.fluid.AunisFluids;
import mrjake.aunis.integration.ThermalIntegration;
import mrjake.aunis.integration.opencomputers.OCWrapperInterface;
import mrjake.aunis.packet.AunisPacketHandler;
import mrjake.aunis.proxy.IProxy;
import mrjake.aunis.worldgen.AunisWorldGen;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod( modid = Aunis.ModID, name = Aunis.Name, version = Aunis.Version, acceptedMinecraftVersions = Aunis.MCVersion, dependencies = "required-after:cofhcore@[4.6.0,);after:opencomputers" )
public class Aunis {	
    public static final String ModID = "aunis";
    public static final String Name = "AUNIS";
    public static final String Version = "1.5.0-beta-hotfix3.1";
    public static final String MCVersion = "[1.12.2]";
 
    public static final boolean DEBUG = false;
    public static final String CLIENT = "mrjake.aunis.proxy.ProxyClient";
    public static final String SERVER = "mrjake.aunis.proxy.ProxyServer";
    
    public static final AunisCreativeTab aunisCreativeTab = new AunisCreativeTab();
    
    @Instance(ModID)
	public static Aunis instance;
    
    @SidedProxy(clientSide = Aunis.CLIENT, serverSide = Aunis.SERVER)
    public static IProxy proxy;
    public static Logger logger;
    
	// ------------------------------------------------------------------------
    // OpenComputers
    private static final String OC_WRAPPER_LOADED = "mrjake.aunis.integration.opencomputers.OCWrapperLoaded";
    private static final String OC_WRAPPER_NOT_LOADED = "mrjake.aunis.integration.opencomputers.OCWrapperNotLoaded";
    
    private static OCWrapperInterface ocWrapper;
    public static OCWrapperInterface getOCWrapper() {
    	if (ocWrapper == null) {
    		try {
				ocWrapper = (OCWrapperInterface) Class.forName(Loader.isModLoaded("opencomputers") ? OC_WRAPPER_LOADED : OC_WRAPPER_NOT_LOADED).newInstance();
			}
    		
    		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
    	}
    	
    	return ocWrapper;
    }
    
	// ------------------------------------------------------------------------
    static {
    	FluidRegistry.enableUniversalBucket();
    }
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        
        AunisPacketHandler.registerPackets();
        AunisFluids.registerFluids();
        
        proxy.preInit(event);
    }
 
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	GameRegistry.registerWorldGenerator(new AunisWorldGen(), 0);
    	
    	ThermalIntegration.registerRecipes();
    	
    	proxy.init(event);
    }
 
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {    	
    	proxy.postInit(event);
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    	event.registerServerCommand(new CommandQueryStargate());
    }
    
	public static void log(String msg) {
		if (DEBUG) {
			logger.info(msg);
		}
	}
	
	public static void info(String msg) {
    	logger.info(msg);
    }
}
