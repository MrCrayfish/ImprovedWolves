package com.mrcrayfish.improvedwolves.network.message;

import com.mrcrayfish.improvedwolves.common.CustomDataParameters;
import com.mrcrayfish.improvedwolves.entity.ai.goal.PutInChestGoal;
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
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
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
    private boolean miss;

    public MessageWolfDepositItem() {}

    public MessageWolfDepositItem(BlockPos pos, boolean miss)
    {
        this.pos = pos;
        this.miss = miss;
    }

    @Override
    public void encode(MessageWolfDepositItem message, PacketBuffer buffer)
    {
        buffer.writeBlockPos(message.pos);
        buffer.writeBoolean(message.miss);
    }

    @Override
    public MessageWolfDepositItem decode(PacketBuffer buffer)
    {
        return new MessageWolfDepositItem(buffer.readBlockPos(), buffer.readBoolean());
    }

    @Override
    public void handle(MessageWolfDepositItem message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                if(!message.miss && !message.pos.withinDistance(player.getEyePosition(0F), 20.0D))
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
                        TileEntity tileEntity = world.getTileEntity(message.pos);
                        if(!message.miss && tileEntity instanceof IInventory && !brain.getMemory(ModMemoryModuleTypes.CHEST).get().getPos().equals(message.pos))
                        {
                            ItemStack heldItem = player.getHeldItemMainhand();
                            if(heldItem.isEmpty())
                            {
                                return;
                            }
                            if(PutInChestGoal.canInsertItemIntoStorage((IInventory) tileEntity, heldItem))
                            {
                                brain.setMemory(ModMemoryModuleTypes.CHEST, GlobalPos.of(player.dimension, message.pos));
                                world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.ENTITY_PLAYER_WHISTLE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            }
                            else
                            {
                                player.sendMessage(new TranslationTextComponent("info.improvedwolves.storage_full", tileEntity.getBlockState().getBlock().getNameTextComponent()), ChatType.GAME_INFO);
                            }
                        }
                        else
                        {
                            brain.setMemory(ModMemoryModuleTypes.CHEST, Optional.empty());
                            world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.ENTITY_PLAYER_WHISTLE, SoundCategory.PLAYERS, 1.0F, 0.5F);
                        }
                    }
                    else
                    {
                        TileEntity tileEntity = world.getTileEntity(message.pos);
                        if(tileEntity instanceof IInventory)
                        {
                            ItemStack heldItem = player.getHeldItemMainhand();
                            if(heldItem.isEmpty())
                            {
                                return;
                            }
                            if(PutInChestGoal.canInsertItemIntoStorage((IInventory) tileEntity, heldItem))
                            {
                                brain.setMemory(ModMemoryModuleTypes.CHEST, GlobalPos.of(player.dimension, message.pos));
                                commandingWolf.goalSelector.getRunningGoals().forEach(PrioritizedGoal::resetTask);
                                world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.ENTITY_PLAYER_WHISTLE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            }
                            else
                            {
                                player.sendMessage(new TranslationTextComponent("info.improvedwolves.storage_full", tileEntity.getBlockState().getBlock().getNameTextComponent()), ChatType.GAME_INFO);
                            }
                        }
                    }
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
