package com.mrcrayfish.improvedwolves.tileentity;

import com.mrcrayfish.improvedwolves.init.ModTileEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class DogBowlTileEntity extends TileEntity
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
