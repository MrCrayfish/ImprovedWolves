package com.mrcrayfish.improvedwolves.util;

import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class InventoryUtil
{
    public static boolean areItemStackEqualIgnoreCount(ItemStack source, ItemStack target)
    {
        if(source.isDamaged())
            return false;
        if(target.isDamaged())
            return false;
        return source.getItem() == target.getItem() && ItemStack.areItemStackTagsEqual(source, target);
    }
}
