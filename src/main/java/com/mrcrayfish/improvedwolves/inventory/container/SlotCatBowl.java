package com.mrcrayfish.improvedwolves.inventory.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Author: MrCrayfish
 */
public class SlotCatBowl extends Slot
{
    public SlotCatBowl(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem().getFood() != null && stack.getItem() == Items.COOKED_COD;
    }
}
