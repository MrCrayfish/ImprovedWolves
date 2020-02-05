package com.mrcrayfish.improvedwolves.tileentity;

import com.mrcrayfish.improvedwolves.init.ModTileEntities;
import com.mrcrayfish.improvedwolves.inventory.container.DogBowlContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class DogBowlTileEntity extends BasicLootTileEntity
{
    public DogBowlTileEntity()
    {
        super(ModTileEntities.DOG_BOWL);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("container.improvedwolves.dog_bowl");
    }

    @Override
    protected Container createMenu(int windowId, PlayerInventory playerInventory)
    {
        return new DogBowlContainer(windowId, playerInventory, this);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        return super.write(compound);
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
}
