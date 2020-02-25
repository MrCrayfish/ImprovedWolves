package com.mrcrayfish.improvedwolves.common;

import com.mrcrayfish.improvedwolves.common.entity.WolfHeldItemDataHandler;
import com.mrcrayfish.improvedwolves.network.PacketHandler;
import com.mrcrayfish.improvedwolves.network.message.MessageSyncHeldWolfItem;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class CommonEvents
{
    @SubscribeEvent
    public void onEntityConstruct(EntityEvent.EntityConstructing event)
    {
        if(event.getEntity() instanceof PlayerEntity)
        {
            event.getEntity().getDataManager().register(CustomDataParameters.COMMANDING_WOLF, Optional.empty());
        }
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking event)
    {
        if(event.getTarget() instanceof WolfEntity)
        {
            WolfEntity wolfEntity = (WolfEntity) event.getTarget();
            WolfHeldItemDataHandler.IWolfHeldItem heldItem = WolfHeldItemDataHandler.getHandler(wolfEntity);
            if(heldItem != null)
            {
                PacketHandler.instance.send(PacketDistributor.TRACKING_ENTITY.with(() -> wolfEntity), new MessageSyncHeldWolfItem(wolfEntity.getEntityId(), heldItem.getItemStack()));
            }
        }
    }
}
