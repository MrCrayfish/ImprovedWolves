package com.mrcrayfish.improvedwolves.common;

import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class CommonEvents
{
    @SubscribeEvent
    public void onEntityConstruct(EntityEvent.EntityConstructing event)
    {
        if(event.getEntity() instanceof WolfEntity)
        {
            event.getEntity().getDataManager().register(CustomDataParameters.WOLF_HELD_ITEM, ItemStack.EMPTY);
        }
        else if(event.getEntity() instanceof PlayerEntity)
        {
            event.getEntity().getDataManager().register(CustomDataParameters.COMMANDING_WOLF, Optional.empty());
        }
    }
}
