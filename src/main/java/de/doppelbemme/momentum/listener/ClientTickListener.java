package de.doppelbemme.momentum.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.text.DecimalFormat;

public class ClientTickListener {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static boolean taskRunning = false;
    public static boolean showRemainingTime = false;
    public static int ticksElapsed = 0;
    private int taskInterval = 100; // 5 seconds, assuming 20 ticks per second

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        ticksElapsed++;
        if (taskRunning && showRemainingTime) {
            double remaining = taskInterval - ticksElapsed;
            remaining = remaining / 20;
            String message = df.format(remaining) + "s";
            Minecraft.getInstance().player.displayClientMessage(Component.literal("§7" + message), true);
        }
        if (ticksElapsed >= taskInterval && taskRunning) {
            executeTask();
            ticksElapsed = 0;
        }
    }

    private void executeTask() {
        Minecraft.getInstance().getConnection().sendCommand("blocks");
        Minecraft.getInstance().getConnection().sendCommand("sellall");
    }
}
