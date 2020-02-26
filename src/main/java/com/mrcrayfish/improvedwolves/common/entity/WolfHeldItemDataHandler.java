package com.mrcrayfish.improvedwolves.common.entity;

import com.mrcrayfish.improvedwolves.Reference;
import com.mrcrayfish.improvedwolves.init.ModMemoryModuleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class WolfHeldItemDataHandler
{
    @CapabilityInject(IWolfHeldItem.class)
    public static final Capability<IWolfHeldItem> CAPABILITY_WOLF_HELD_ITEM = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IWolfHeldItem.class, new Storage(), WolfHeldItem::new);
        MinecraftForge.EVENT_BUS.register(new WolfHeldItemDataHandler());
    }

    @Nullable
    public static IWolfHeldItem getHandler(WolfEntity wolf)
    {
        return wolf.getCapability(CAPABILITY_WOLF_HELD_ITEM, Direction.DOWN).orElse(null);
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof WolfEntity)
        {
            event.addCapability(new ResourceLocation(Reference.MOD_ID, "wolf_held_item"), new Provider());
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event)
    {
        if(event.getEntity() instanceof WolfEntity)
        {
            WolfEntity wolf = (WolfEntity) event.getEntity();
            WolfHeldItemDataHandler.IWolfHeldItem handler = WolfHeldItemDataHandler.getHandler(wolf);
            if(handler != null)
            {
                ItemStack stack = handler.getItemStack();
                if(!stack.isEmpty())
                {
                    MinecraftServer server = event.getWorld().getServer();
                    if(server != null)
                    {
                        wolf.getBrain().setMemory(ModMemoryModuleTypes.CHEST, Optional.empty());
                        handler.setItemStack(ItemStack.EMPTY);
                        server.enqueue(new TickDelayedTask(1, () -> event.getWorld().addEntity(new ItemEntity(event.getWorld(), wolf.getPosX(), wolf.getPosY(), wolf.getPosZ(), stack.copy()))));
                    }
                }
            }
        }
    }

    public interface IWolfHeldItem
    {
        void setItemStack(ItemStack stack);

        ItemStack getItemStack();
    }

    public static class WolfHeldItem implements IWolfHeldItem
    {
        private ItemStack stack = ItemStack.EMPTY;

        @Override
        public void setItemStack(ItemStack stack)
        {
            this.stack = stack;
        }

        @Override
        public ItemStack getItemStack()
        {
            return stack;
        }
    }

    public static class Storage implements Capability.IStorage<IWolfHeldItem>
    {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IWolfHeldItem> capability, IWolfHeldItem instance, Direction side)
        {
            return instance.getItemStack().write(new CompoundNBT());
        }

        @Override
        public void readNBT(Capability<IWolfHeldItem> capability, IWolfHeldItem instance, Direction side, INBT nbt)
        {
            instance.setItemStack(ItemStack.read((CompoundNBT) nbt));
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT>
    {
        final IWolfHeldItem INSTANCE = CAPABILITY_WOLF_HELD_ITEM.getDefaultInstance();

        @Override
        public CompoundNBT serializeNBT()
        {
            return (CompoundNBT) CAPABILITY_WOLF_HELD_ITEM.getStorage().writeNBT(CAPABILITY_WOLF_HELD_ITEM, INSTANCE, null);
        }

        @Override
        public void deserializeNBT(CompoundNBT compound)
        {
            CAPABILITY_WOLF_HELD_ITEM.getStorage().readNBT(CAPABILITY_WOLF_HELD_ITEM, INSTANCE, null, compound);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return CAPABILITY_WOLF_HELD_ITEM.orEmpty(cap, LazyOptional.of(() -> INSTANCE));
        }
    }
}
