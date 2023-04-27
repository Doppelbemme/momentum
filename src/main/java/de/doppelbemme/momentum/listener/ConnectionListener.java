package de.doppelbemme.momentum.listener;

import de.doppelbemme.momentum.Momentum;
import io.netty.channel.ChannelPipeline;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ConnectionListener {
    @SubscribeEvent
    public void onConnect(ClientPlayerNetworkEvent.LoggingIn event) {
        Momentum.connected = true;
        if(Momentum.firstConnection) {
            Momentum.firstConnection = false;
            ChannelPipeline pipeline = event.getConnection().channel().pipeline();
            pipeline.addBefore("packet_handler","main", new PacketListener());
        }

    }

    @SubscribeEvent (priority = EventPriority.HIGHEST)
    public void onDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        Momentum.connected = false;
        Momentum.firstConnection = true;
    }
}
