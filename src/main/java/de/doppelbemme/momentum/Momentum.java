package de.doppelbemme.momentum;

import com.mojang.logging.LogUtils;
import de.doppelbemme.momentum.listener.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Momentum.MODID)
public class Momentum {
    public static final String MODID = "momentum";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static boolean singlebreak = false;
    public static boolean firstConnection = true;
    public static boolean connected = false;
    public static boolean particleAim = false;
    public static boolean safeBreak = false;

    public Momentum() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new KeyInputListener());
        MinecraftForge.EVENT_BUS.register(new ClientTickListener());
        MinecraftForge.EVENT_BUS.register(new ConnectionListener());
        MinecraftForge.EVENT_BUS.register(new PacketListener());
        MinecraftForge.EVENT_BUS.register(new EventSubscribers());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
        }
    }

    public static void startTask() {
        ClientTickListener.taskRunning = true;
        ClientTickListener.ticksElapsed = 0;
    }

    public static void stopTask() {
        ClientTickListener.taskRunning = false;
    }
}
