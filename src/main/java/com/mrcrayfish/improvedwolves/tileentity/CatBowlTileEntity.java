package com.mrcrayfish.improvedwolves.tileentity;

import com.mrcrayfish.improvedwolves.init.ModTileEntities;
import com.mrcrayfish.improvedwolves.inventory.container.CatBowlContainer;
import com.mrcrayfish.improvedwolves.util.TileEntityUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class CatBowlTileEntity extends BasicLootTileEntity
{
    public CatBowlTileEntity()
    {
        super(ModTileEntities.Cat_BOWL);
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("container.improvedwolves.cat_bowl");
    }

    @Override
    protected Container createMenu(int windowId, PlayerInventory playerInventory)
    {
        return new CatBowlContainer(windowId, playerInventory, this);
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        this.read(pkt.getNbtCompound());
    }

    public boolean hasFood()
    {
        ItemStack stack = this.getStackInSlot(0);
        return !stack.isEmpty() && stack.getItem().getFood() != null;
    }

    @Nullable
    public Food getFood()
    {
        ItemStack stack = this.getStackInSlot(0);
        if(!stack.isEmpty())
        {
            return stack.getItem().getFood();
        }
        return null;
    }

    public void consumeFood()
    {
        ItemStack stack = this.getStackInSlot(0);
        if(!stack.isEmpty())
        {
            stack.shrink(1);
            TileEntityUtil.sendUpdatePacket(this);
        }
    }
}
