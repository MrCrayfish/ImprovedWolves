package com.mrcrayfish.improvedwolves.common;

import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

import java.util.Optional;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class CustomDataParameters
{
    public static final DataParameter<ItemStack> WOLF_HELD_ITEM = EntityDataManager.createKey(WolfEntity.class, DataSerializers.ITEMSTACK);
    public static final DataParameter<Optional<UUID>> COMMANDING_WOLF = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
}
