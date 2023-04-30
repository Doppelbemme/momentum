package de.doppelbemme.momentum.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.text.DecimalFormat;

public class ClientTickListener {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static boolean taskRunning = false;
    public static boolean showRemainingTime = false;
    public static int ticksElapsed = 0;
    private int taskInterval = 20; // 5 seconds, assuming 20 ticks per second

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        ticksElapsed++;
        /*
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
        */

        if (ticksElapsed >= taskInterval && taskRunning) {
            checkInventoryFull(player);
            ticksElapsed = 0;
        }

    }

    public void checkInventoryFull(Player player){
        Inventory inventory = player.getInventory();

        int slots = inventory.getContainerSize();
        int filledSlots = 0;

        for(int i = 0; i<slots; i++){
            ItemStack stack = inventory.getItem(i);
            if(!stack.isEmpty()){
                filledSlots++;
            }
        }

        if (filledSlots == slots){
            executeTask();
        }

    }

    private void executeTask() {
        Minecraft.getInstance().getConnection().sendCommand("blocks");
        Minecraft.getInstance().getConnection().sendCommand("sellall");
    }
}
