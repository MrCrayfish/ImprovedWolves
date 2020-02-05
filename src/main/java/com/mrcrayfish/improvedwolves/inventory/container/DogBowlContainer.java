package com.mrcrayfish.improvedwolves.inventory.container;

import com.mrcrayfish.improvedwolves.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class DogBowlContainer extends Container
{
    private IInventory inventory;

    public DogBowlContainer(int windowId, PlayerInventory playerInventory)
    {
        this(windowId, playerInventory, new Inventory(1));
    }

    public DogBowlContainer(int windowId, PlayerInventory playerInventory, IInventory inventory)
    {
        super(ModContainers.DOG_BOWL, windowId);
        assertInventorySize(inventory, 1);
        inventory.openInventory(playerInventory.player);
        this.inventory = inventory;

        this.addSlot(new SlotDogBowl(inventory, 0, 80, 20));

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
            }
        }

        for(int i = 0; i < 9; i++)
        {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return this.inventory.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack copyStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            copyStack = slotStack.copy();
            if(index < 1)
            {
                if(!this.mergeItemStack(slotStack, 1, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(slotStack, 0, 1, false))
            {
                return ItemStack.EMPTY;
            }

            if(slotStack.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        return copyStack;
    }
}
