package com.mrcrayfish.improvedwolves.common.entity;

import com.mrcrayfish.improvedwolves.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class PlayerDataHandler
{
    @CapabilityInject(IPlayerData.class)
    public static final Capability<IPlayerData> CAPABILITY_PLAYER_DATA = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataHandler.Storage(), PlayerData::new);
        MinecraftForge.EVENT_BUS.register(new PlayerDataHandler());
    }

    @Nullable
    public static IPlayerData getHandler(PlayerEntity player)
    {
        return player.getCapability(CAPABILITY_PLAYER_DATA, Direction.DOWN).orElse(null);
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof PlayerEntity)
        {
            event.addCapability(new ResourceLocation(Reference.MOD_ID, "wolf_data"), new PlayerDataHandler.Provider());
        }
    }

    public interface IPlayerData
    {
        void setCommandingWolf(UUID uuid);

        Optional<UUID> getCommandingWolf();
    }

    public static class PlayerData implements IPlayerData
    {
        private UUID commandingWolfUuid = null;

        @Override
        public void setCommandingWolf(UUID uuid)
        {
            this.commandingWolfUuid = uuid;
        }

        @Override
        public Optional<UUID> getCommandingWolf()
        {
            return Optional.ofNullable(this.commandingWolfUuid);
        }
    }

    public static class Storage implements Capability.IStorage<IPlayerData>
    {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IPlayerData> capability, IPlayerData instance, Direction side)
        {
            CompoundNBT compound = new CompoundNBT();
            if(instance.getCommandingWolf().isPresent())
            {
                compound.putUniqueId("CommandingWolfUUID", instance.getCommandingWolf().get());
            }
            return compound;
        }

        @Override
        public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, Direction side, INBT nbt)
        {
            CompoundNBT compound = (CompoundNBT) nbt;
            if(compound.hasUniqueId("CommandingWolfUUID"))
            {
                instance.setCommandingWolf(compound.getUniqueId("CommandingWolfUUID"));
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT>
    {
        final IPlayerData INSTANCE = CAPABILITY_PLAYER_DATA.getDefaultInstance();

        @Override
        public CompoundNBT serializeNBT()
        {
            return (CompoundNBT) CAPABILITY_PLAYER_DATA.getStorage().writeNBT(CAPABILITY_PLAYER_DATA, INSTANCE, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT compound)
        {
            CAPABILITY_PLAYER_DATA.getStorage().readNBT(CAPABILITY_PLAYER_DATA, INSTANCE, null, compound);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return CAPABILITY_PLAYER_DATA.orEmpty(cap, LazyOptional.of(() -> INSTANCE));
        }
    }
}
