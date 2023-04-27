package de.doppelbemme.momentum.listener;

import de.doppelbemme.momentum.Momentum;
import de.doppelbemme.momentum.event.PacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventSubscribers {
    private long nextBreak = 0;
    private long nextParticleAim = 0;
    private double lowestDistance = 99.9;
    private Vec3 lastParticlePos = null;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void packetDisplay(PacketEvent event) {
        if (!Momentum.particleAim) {
            return;
        }
        if (!(event.getPacket() instanceof ClientboundLevelParticlesPacket)) {
            return;
        }
        if (nextParticleAim > System.currentTimeMillis()) {
            return;
        }

        double playerX = Minecraft.getInstance().player.getX();
        double playerY = Minecraft.getInstance().player.getY();
        double playerZ = Minecraft.getInstance().player.getZ();
        Vec3 playerPos = new Vec3(playerX, playerY, playerZ);

        double x = ((ClientboundLevelParticlesPacket) event.getPacket()).getX();
        double y = ((ClientboundLevelParticlesPacket) event.getPacket()).getY();
        double z = ((ClientboundLevelParticlesPacket) event.getPacket()).getZ();
        Vec3 particlePos = new Vec3(x, y, z);

        if (playerPos.distanceTo(particlePos) > 5 || playerPos.distanceTo(particlePos) > lowestDistance) {
            return;
        }

        Minecraft.getInstance().player.lookAt(EntityAnchorArgument.Anchor.EYES, particlePos);
        lowestDistance = playerPos.distanceTo(particlePos);
        lastParticlePos = particlePos;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void outgoing(PacketEvent.Outgoing event) {
        Packet<?> packet = event.getPacket();
        if (Momentum.singlebreak && packet instanceof ServerboundPlayerActionPacket) {
            ServerboundPlayerActionPacket playerActionPacket = (ServerboundPlayerActionPacket) packet;
            BlockPos blockPos = playerActionPacket.getPos();
            if (playerActionPacket.getAction() == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK && nextBreak > System.currentTimeMillis()) {
                Minecraft.getInstance().player.displayClientMessage(Component.literal("§cCancelled"), true);
                event.setCanceled(true);
                BlockState blockState = Minecraft.getInstance().level.getBlockState(blockPos);
                Packet<?> blockChangePacket = new ClientboundBlockUpdatePacket(blockPos, blockState);
                event.setPacket(blockChangePacket);
                return;
            }
            if(Momentum.safeBreak && lastParticlePos.distanceTo(Vec3.atCenterOf(blockPos)) > 0.80) {
                Minecraft.getInstance().player.displayClientMessage(Component.literal("§cCancelled"), true);
                //Minecraft.getInstance().player.displayClientMessage(Component.literal(lastParticlePos.distanceTo(Vec3.atCenterOf(blockPos)) + ""), true);
                event.setCanceled(true);
                BlockState blockState = Minecraft.getInstance().level.getBlockState(blockPos);
                Packet<?> blockChangePacket = new ClientboundBlockUpdatePacket(blockPos, blockState);
                event.setPacket(blockChangePacket);
                return;
            }
            nextBreak = System.currentTimeMillis() + 500;
            nextParticleAim = System.currentTimeMillis() + 100;
            lowestDistance = 99.9;
        }
    }
}