package com.mrcrayfish.improvedwolves.inventory.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class SlotDogBowl extends Slot
{
    public SlotDogBowl(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem().getFood() != null && stack.getItem().getFood().isMeat();
    }
}
