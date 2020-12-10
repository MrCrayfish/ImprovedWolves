package com.mrcrayfish.improvedwolves.inventory.container;

import com.mrcrayfish.improvedwolves.init.ModContainers;
import com.mrcrayfish.improvedwolves.tileentity.CatBowlTileEntity;
import com.mrcrayfish.improvedwolves.util.TileEntityUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

/**
 * Author: MrCrayfish
 */
public class CatBowlContainer extends Container
{
    private PlayerInventory playerInventory;
    private IInventory inventory;

    public CatBowlContainer(int windowId, PlayerInventory playerInventory)
    {
        this(windowId, playerInventory, new Inventory(1));
    }

    public CatBowlContainer(int windowId, PlayerInventory playerInventory, IInventory inventory)
    {
        super(ModContainers.Cat_BOWL, windowId);
        assertInventorySize(inventory, 1);
        inventory.openInventory(playerInventory.player);
        this.inventory = inventory;
        this.playerInventory = playerInventory;

        this.addSlot(new SlotCatBowl(inventory, 0, 80, 20));

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

        this.addListener(new IContainerListener()
        {
            @Override
            public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {}

            @Override
            public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
            {
                if(slotInd == 0 && inventory instanceof CatBowlTileEntity)
                {
                    TileEntityUtil.sendUpdatePacket((CatBowlTileEntity) inventory);
                }
            }

            @Override
            public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {}
        });
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
