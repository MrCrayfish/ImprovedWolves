package com.mrcrayfish.improvedwolves.network.message;

import com.mrcrayfish.improvedwolves.client.ClientHandler;
import com.mrcrayfish.improvedwolves.common.entity.WolfHeldItemDataHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageSyncHeldWolfItem implements IMessage<MessageSyncHeldWolfItem>
{
    private int entityId;
    private ItemStack stack;

    public MessageSyncHeldWolfItem() {}

    public MessageSyncHeldWolfItem(int entityId, ItemStack stack)
    {
        this.entityId = entityId;
        this.stack = stack;
    }

    @Override
    public void encode(MessageSyncHeldWolfItem message, PacketBuffer buffer)
    {
        buffer.writeVarInt(message.entityId);
        buffer.writeItemStack(message.stack);
    }

    @Override
    public MessageSyncHeldWolfItem decode(PacketBuffer buffer)
    {
        return new MessageSyncHeldWolfItem(buffer.readVarInt(), buffer.readItemStack());
    }

    @Override
    public void handle(MessageSyncHeldWolfItem message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientHandler.setWolfHeldItem(message.entityId, message.stack));
        supplier.get().setPacketHandled(true);
    }
}
