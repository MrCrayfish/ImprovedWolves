package com.mrcrayfish.improvedwolves.network;

import com.mrcrayfish.improvedwolves.Reference;
import com.mrcrayfish.improvedwolves.network.message.IMessage;
import com.mrcrayfish.improvedwolves.network.message.MessageSyncHeldWolfItem;
import com.mrcrayfish.improvedwolves.network.message.MessageWolfDepositItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler
{
    public static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel instance;
    private static int nextId = 0;

    public static void register()
    {
        instance = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Reference.MOD_ID, "network"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .simpleChannel();

        register(MessageWolfDepositItem.class, new MessageWolfDepositItem());
        register(MessageSyncHeldWolfItem.class, new MessageSyncHeldWolfItem());
    }

    private static <T> void register(Class<T> clazz, IMessage<T> message)
    {
        instance.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }
}