package com.mrcrayfish.improvedwolves.network.message;

import com.mrcrayfish.improvedwolves.common.CustomDataParameters;
import com.mrcrayfish.improvedwolves.init.ModMemoryModuleTypes;
import com.mrcrayfish.improvedwolves.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageWolfDepositItem implements IMessage<MessageWolfDepositItem>
{
    private BlockPos pos;

    public MessageWolfDepositItem() {}

    public MessageWolfDepositItem(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public void encode(MessageWolfDepositItem message, PacketBuffer buffer)
    {
        buffer.writeBlockPos(message.pos);
    }

    @Override
    public MessageWolfDepositItem decode(PacketBuffer buffer)
    {
        return new MessageWolfDepositItem(buffer.readBlockPos());
    }

    @Override
    public void handle(MessageWolfDepositItem message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                if(!message.pos.withinDistance(player.getEyePosition(0F), 20.0D))
                {
                    return;
                }

                ServerWorld world = player.getServerWorld();
                WolfEntity commandingWolf = null;
                Optional<UUID> optional = player.getDataManager().get(CustomDataParameters.COMMANDING_WOLF);
                if(optional.isPresent())
                {
                    UUID uuid = optional.get();
                    List<WolfEntity> wolves = world.getEntitiesWithinAABB(EntityType.WOLF, player.getBoundingBox().grow(20), wolf -> wolf.isTamed() && wolf.getUniqueID().equals(uuid) && wolf.getOwnerId() != null && wolf.getOwnerId().equals(player.getUniqueID()));
                    if(wolves.size() > 0)
                    {
                        commandingWolf = wolves.get(0);
                    }
                    else
                    {
                        player.getDataManager().set(CustomDataParameters.COMMANDING_WOLF, Optional.empty());
                    }
                }
                if(commandingWolf == null)
                {
                    List<WolfEntity> wolves = world.getEntitiesWithinAABB(EntityType.WOLF, player.getBoundingBox().grow(20), wolf -> wolf.isTamed() && wolf.getOwnerId() != null && wolf.getOwnerId().equals(player.getUniqueID()));
                    commandingWolf = wolves.stream().min(Comparator.comparing(player::getDistance)).orElse(null);
                }
                if(commandingWolf != null)
                {
                    if(commandingWolf.isSitting())
                    {
                        return;
                    }
                    if(commandingWolf.getNavigator().getPathToEntity(player, 1) == null)
                    {
                        return;
                    }
                    player.getDataManager().set(CustomDataParameters.COMMANDING_WOLF, Optional.of(commandingWolf.getUniqueID()));
                    Brain<?> brain = commandingWolf.getBrain();
                    if(brain.getMemory(ModMemoryModuleTypes.CHEST).isPresent())
                    {
                        if(world.getTileEntity(message.pos) instanceof IInventory && !brain.getMemory(ModMemoryModuleTypes.CHEST).get().getPos().equals(message.pos))
                        {
                            brain.setMemory(ModMemoryModuleTypes.CHEST, GlobalPos.of(player.dimension, message.pos));
                            world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.ENTITY_PLAYER_WHISTLE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        }
                        else
                        {
                            brain.setMemory(ModMemoryModuleTypes.CHEST, Optional.empty());
                            world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.ENTITY_PLAYER_WHISTLE, SoundCategory.PLAYERS, 1.0F, 0.5F);
                        }
                    }
                    else if(world.getTileEntity(message.pos) instanceof IInventory)
                    {
                        brain.setMemory(ModMemoryModuleTypes.CHEST, GlobalPos.of(player.dimension, message.pos));
                        commandingWolf.goalSelector.getRunningGoals().forEach(PrioritizedGoal::resetTask);
                        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.ENTITY_PLAYER_WHISTLE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
