package de.doppelbemme.momentum.listener;

import de.doppelbemme.momentum.Momentum;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyInputListener {
    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        Player player = Minecraft.getInstance().player;
        int action = event.getAction();
        int key = event.getKey();
        if (action == 1 && Momentum.connected) {
            if (key == 327) { //Toggle Singlebreak (NumPad 7)
                Momentum.singlebreak = !Momentum.singlebreak;
                if (Momentum.singlebreak) {
                    player.displayClientMessage(Component.literal("§eSingleBreak §7» §aAktiviert"), true);
                } else {
                    player.displayClientMessage(Component.literal("§eSingleBreak §7» §cDeaktiviert"), true);
                }
            } else if (key == 328) { //Toggle Autosell (NumPad 8)
                if (!ClientTickListener.taskRunning) {
                    player.displayClientMessage(Component.literal("§eAutosell §7» §aAktiviert"), true);
                    Momentum.startTask();
                } else {
                    player.displayClientMessage(Component.literal("§eAutosell §7» §cDeaktiviert"), true);
                    Momentum.stopTask();
                }
            } else if (key == 329) { //Toggle Remaining Time (NumPad 9)
                ClientTickListener.showRemainingTime = !ClientTickListener.showRemainingTime;
                if (ClientTickListener.showRemainingTime) {
                    player.displayClientMessage(Component.literal("§eRemaining Time §7» §aAktiviert"), true);
                } else {
                    player.displayClientMessage(Component.literal("§eRemaining Time §7» §cDeaktiviert"), true);
                }
            } else if (key == 326) { //Toggle Particle Aim (NumPad 6)
                Momentum.particleAim = !Momentum.particleAim;
                if (Momentum.particleAim) {
                    player.displayClientMessage(Component.literal("§eParticleAim §7» §aAktiviert"), true);
                } else {
                    player.displayClientMessage(Component.literal("§eParticleAim §7» §cDeaktiviert"), true);
                }
            } else if (key == 325) { //Toggle SafeBreak (NumPad 5)
                Momentum.safeBreak = !Momentum.safeBreak;
                if (Momentum.safeBreak) {
                    player.displayClientMessage(Component.literal("§eSafeBreak §7» §aAktiviert"), true);
                } else {
                    player.displayClientMessage(Component.literal("§eSafeBreak §7» §cDeaktiviert"), true);
                }
            } else if (key == 324) {
                int moonPhase = player.getLevel().getMoonPhase();
                player.displayClientMessage(Component.literal("§eMoon Phase §7» §e" + moonPhase), true);
            }
        }
    }
}
