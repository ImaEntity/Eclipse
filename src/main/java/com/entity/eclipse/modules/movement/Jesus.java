package com.entity.eclipse.modules.movement;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleType;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvent;
import com.entity.eclipse.utils.types.BooleanValue;
import com.entity.eclipse.utils.types.DoubleValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import com.google.common.collect.Streams;
import net.minecraft.util.shape.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Jesus extends Module {
    private final BlockPos.Mutable blockPos = new BlockPos.Mutable();
    private int packetTimer = 0;
    private int tickTimer = 10;

    public Jesus() {
        super("Jesus", "Allows you to walk on water and lava.", ModuleType.MOVEMENT);

        this.config.create("WaterSinkIfBurning", new BooleanValue(true));
        this.config.create("WaterSinkIfSneak", new BooleanValue(true));
        this.config.create("WaterSinkOnFall", new BooleanValue(true));
        this.config.create("WaterSinkFallHeight", new DoubleValue(3.0));

        this.config.create("LavaSinkIfSneak", new BooleanValue(false));
        this.config.create("LavaSinkOnFall", new BooleanValue(false));
        this.config.create("LavaSinkFallHeight", new DoubleValue(3.0));

        Events.Packet.register(PacketEvents.SEND, event -> {
            if(!this.isEnabled()) return;

            if(Eclipse.client.player == null) return;
            if(Eclipse.client.getNetworkHandler() == null) return;

            if(!(event.getPacket() instanceof PlayerMoveC2SPacket packet)) return;
            if(Eclipse.client.player.isTouchingWater() && !this.waterIsSolid()) return;
            if(Eclipse.client.player.isInLava() && !this.lavaIsSolid()) return;

            if(!(packet instanceof PlayerMoveC2SPacket.PositionAndOnGround || packet instanceof PlayerMoveC2SPacket.Full)) return;

            if(
                    Eclipse.client.player.isTouchingWater() ||
                    Eclipse.client.player.isInLava() ||
                    Eclipse.client.player.fallDistance > 3f ||
                    !this.isOverLiquid()
            ) return;

            if(Eclipse.client.player.input.movementForward == 0 && Eclipse.client.player.input.movementSideways == 0) {
                event.setCancelled(true);
                return;
            }

            if(this.packetTimer++ < 4) return;
            this.packetTimer = 0;

            event.setCancelled(true);

            double x = packet.getX(0);
            double y = packet.getY(0) + 0.05;
            double z = packet.getZ(0);

            Packet<?> newPacket;

            if(packet instanceof PlayerMoveC2SPacket.PositionAndOnGround)
                newPacket = new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, true);
            else
                newPacket = new PlayerMoveC2SPacket.Full(x, y, z, packet.getYaw(0), packet.getPitch(0), true);

            Eclipse.client.getNetworkHandler().getConnection().send(newPacket);
        });
    }

    public boolean lavaIsSolid() {
        if(Eclipse.client.player == null) return true;

        boolean sinkOnSneak = this.config.get("LavaSinkIfSneak");
        boolean sinkOnFall = this.config.get("LavaSinkOnFall");

        double sinkFallHeight = this.config.get("LavaSinkFallHeight");

        if(sinkOnSneak && Eclipse.client.options.sneakKey.isPressed()) return false;
        if(sinkOnFall && Eclipse.client.player.fallDistance >= sinkFallHeight) return false;

        return true;
    }

    public boolean waterIsSolid() {
        if(Eclipse.client.player == null) return true;

        if(Eclipse.client.player.hasVehicle()) {
            EntityType<?> vehicle = Eclipse.client.player.getVehicle().getType();
            if(vehicle == EntityType.BOAT || vehicle == EntityType.CHEST_BOAT) return false;
        }

        boolean sinkIfBurning = this.config.get("WaterSinkIfBurning");
        boolean sinkOnSneak = this.config.get("WaterSinkIfSneak");
        boolean sinkOnFall = this.config.get("WaterSinkOnFall");

        double sinkFallHeight = this.config.get("WaterSinkFallHeight");

        if(sinkIfBurning && Eclipse.client.player.isOnFire()) return false;
        if(sinkOnSneak && Eclipse.client.options.sneakKey.isPressed()) return false;
        if(sinkOnFall && Eclipse.client.player.fallDistance >= sinkFallHeight) return false;

        return true;
    }

    private boolean isOverLiquid() {
        if(Eclipse.client.world == null) return false;
        if(Eclipse.client.player == null) return false;

        boolean foundLiquid = false;
        boolean foundSolid = false;

        List<Box> blockCollisions = Streams.stream(Eclipse.client.world.getBlockCollisions(
                Eclipse.client.player,
                Eclipse.client.player.getBoundingBox().offset(0, -0.5, 0)
        ))
                .map(VoxelShape::getBoundingBox)
                .collect(Collectors.toCollection(ArrayList::new));

        for(Box bb : blockCollisions) {
            this.blockPos.set(
                    MathHelper.lerp(0.5D, bb.minX, bb.maxX),
                    MathHelper.lerp(0.5D, bb.minY, bb.maxY),
                    MathHelper.lerp(0.5D, bb.minZ, bb.maxZ)
            );

            BlockState blockState = Eclipse.client.world.getBlockState(blockPos);

            if(
                    (blockState.getBlock() == Blocks.WATER || blockState.getFluidState().getFluid() == Fluids.WATER) ||
                    (blockState.getBlock() == Blocks.LAVA || blockState.getFluidState().getFluid() == Fluids.LAVA)
            ) foundLiquid = true;
            else if(!blockState.isAir())
                foundSolid = true;
        }

        return foundLiquid && !foundSolid;
    }

    @Override
    public void tick() {
        if(Eclipse.client.player == null) return;
        if(Eclipse.client.world == null) return;

        if(Eclipse.client.player.isTouchingWater() && !this.waterIsSolid()) return;
        if(Eclipse.client.player.isInSwimmingPose()) return;
        if(Eclipse.client.player.isInLava() && !this.lavaIsSolid()) return;

        BlockState below = Eclipse.client.world.getBlockState(Eclipse.client.player.getBlockPos().down());
        if(below.getBlock() != Blocks.WATER && below.getBlock() != Blocks.LAVA) return;

        boolean waterlogged = false;
        try { waterlogged = below.get(Properties.WATERLOGGED); } catch(Exception ignored) {}

        if(Eclipse.client.player.isTouchingWater() || Eclipse.client.player.isInLava()) {
            Vec3d vel = Eclipse.client.player.getVelocity();

            Eclipse.client.player.setVelocity(
                    vel.x,
                    0.11,
                    vel.z
            );

            this.tickTimer = 0;
            return;
        }

        if(this.tickTimer == 0) {
            Vec3d vel = Eclipse.client.player.getVelocity();
            Eclipse.client.player.setVelocity(
                    vel.x,
                    0.3,
                    vel.z
            );
        } else if(this.tickTimer == 1 && (below.getBlock() == Blocks.WATER || below.getBlock() == Blocks.LAVA || waterlogged))
            Eclipse.client.player.getVelocity().multiply(1, 0, 1);

        this.tickTimer++;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void renderWorld(RenderEvent event) {

    }

    @Override
    public void renderScreen(RenderEvent event) {

    }
}
