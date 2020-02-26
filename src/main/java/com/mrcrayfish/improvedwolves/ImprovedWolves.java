package com.mrcrayfish.improvedwolves;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.Dynamic;
import com.mrcrayfish.improvedwolves.block.DogBowlBlock;
import com.mrcrayfish.improvedwolves.block.CatBowlBlock;
import com.mrcrayfish.improvedwolves.client.ClientHandler;
import com.mrcrayfish.improvedwolves.client.render.tileentity.DogBowlRenderer;
import com.mrcrayfish.improvedwolves.client.render.tileentity.CatBowlRenderer;
import com.mrcrayfish.improvedwolves.client.screen.DogBowlScreen;
import com.mrcrayfish.improvedwolves.client.screen.CatBowlScreen;
import com.mrcrayfish.improvedwolves.common.CommonEvents;
import com.mrcrayfish.improvedwolves.common.entity.PlayerDataHandler;
import com.mrcrayfish.improvedwolves.common.entity.WolfHeldItemDataHandler;
import com.mrcrayfish.improvedwolves.entity.ai.goal.MoveToDogBowlGoal;
import com.mrcrayfish.improvedwolves.entity.ai.goal.MoveToCatBowlGoal;
import com.mrcrayfish.improvedwolves.entity.ai.goal.PutInChestGoal;
import com.mrcrayfish.improvedwolves.init.ModBlocks;
import com.mrcrayfish.improvedwolves.init.ModContainers;
import com.mrcrayfish.improvedwolves.init.ModMemoryModuleTypes;
import com.mrcrayfish.improvedwolves.init.ModTileEntities;
import com.mrcrayfish.improvedwolves.network.PacketHandler;
import com.mrcrayfish.improvedwolves.tileentity.DogBowlTileEntity;
import com.mrcrayfish.improvedwolves.tileentity.CatBowlTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Author: MrCrayfish
 */
@Mod(Reference.MOD_ID)
public class ImprovedWolves
{
    public static final ItemGroup GROUP = new ItemGroup(Reference.MOD_ID)
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModBlocks.RED_DOG_BOWL);
        }
    };

    public ImprovedWolves()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
        WolfHeldItemDataHandler.register();
        PlayerDataHandler.register();
        PacketHandler.register();
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        ClientHandler.setup();
    }

    /* Linked via ASM in MobEntity */
    @SuppressWarnings("unused")
    public static void registerGoals(MobEntity entity)
    {
        if(entity instanceof WolfEntity)
        {
            WolfEntity wolfEntity = (WolfEntity) entity;
            wolfEntity.goalSelector.addGoal(5, new MoveToDogBowlGoal(wolfEntity));
            wolfEntity.goalSelector.addGoal(5, new PutInChestGoal(wolfEntity));
        }
        if(entity instanceof CatEntity)
        {
            CatEntity catEntity = (CatEntity) entity;
            catEntity.goalSelector.addGoal(5, new MoveToCatBowlGoal(catEntity));
        }
    }

    /* Linked via ASM in LivingEntity */
    @SuppressWarnings("unused")
    public static Brain<?> createBrain(LivingEntity entity, Dynamic<?> dynamic)
    {
        if(entity instanceof WolfEntity)
        {
            return new Brain<>(ImmutableList.of(ModMemoryModuleTypes.CHEST), ImmutableList.of(), dynamic);
        }
        return new Brain<>(ImmutableList.of(), ImmutableList.of(), dynamic);
    }
}
