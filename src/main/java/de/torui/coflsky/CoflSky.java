package de.torui.coflsky;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

import org.lwjgl.input.Keyboard;

import de.torui.coflsky.minecraft_integration.PlayerDataProvider;
import de.torui.coflsky.minecraft_integration.TemporarySession;
import de.torui.coflsky.websocket.WSClientWrapper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = CoflSky.MODID, version = CoflSky.VERSION)
public class CoflSky
{
    public static final String MODID = "CoflSky";
    public static final String VERSION = "1.1-Alpha";
    
    public static WSClientWrapper Wrapper;
    public static KeyBinding[] keyBindings;


    
    @EventHandler
    public void init(FMLInitializationEvent event) throws URISyntaxException
    {
    	
    	System.out.println(">>>> "+ TemporarySession.GetTempFileFolder().toString());
    	//Minecraft.getSessionInfo().forEach((a,b) -> System.out.println("Key=" + a + " value=" + b));
    	
    	//System.out.println("Loggerfactory: " + LoggerFactory.getILoggerFactory());
    //	Logger log = LoggerFactory.getLogger(CoflSky.class);
   // 	log.debug("Testing");
    	
    	  
    	
		// some example code
        System.out.println("Initializing");
        
        //new Thread(new WSClient(new URI("ws://localhost:8080"))).start();        
        System.out.println(">>>Started");
        
        String username = PlayerDataProvider.getUsername();
        String uuid = PlayerDataProvider.getActivePlayerUUID();
        System.out.println(">>> Username= " + username + " with UUID=" + uuid ); 
        
        String tempUUID = UUID.randomUUID().toString();
        try {
			TemporarySession.UpdateSessions();
			tempUUID = TemporarySession.GetSession(username).SessionUUID;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        //CoflSky.Wrapper = new WSClientWrapper("wss://sky-commands.coflnet.com/modsocket?version=" + CoflSky.VERSION  + "&uuid=");
        CoflSky.Wrapper = new WSClientWrapper("ws://sky-mod.coflnet.com/modsocket?version=" + CoflSky.VERSION + "&SId=" + tempUUID  + "&uuid=" + uuid);
        
        keyBindings = new KeyBinding[] {
        		new KeyBinding("key.replay_last.onclick", Keyboard.KEY_R, "SkyCofl")
        };
        
        if(event.getSide() == Side.CLIENT) {
        	ClientCommandHandler.instance.registerCommand(new CoflSkyCommand());
        	
        	for (int i = 0; i < keyBindings.length; ++i) 
        	{
        	    ClientRegistry.registerKeyBinding(keyBindings[i]);
        	}
        	
        	
        }        	
        MinecraftForge.EVENT_BUS.register(new EventRegistry());	   
    }   


   /* @EventHandler
    public void init(FMLServerStartingEvent event)
    {
    	
    	if(event.getSide() == Side.CLIENT)    	return;
    		//event.registerServerCommand(new CoflSkyCommand());
    }*/
    
}
	