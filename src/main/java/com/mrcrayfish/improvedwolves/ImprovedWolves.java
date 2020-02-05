package com.mrcrayfish.improvedwolves;

import com.mrcrayfish.improvedwolves.block.DogBowlBlock;
import com.mrcrayfish.improvedwolves.client.ClientHandler;
import com.mrcrayfish.improvedwolves.client.render.tileentity.DogBowlRenderer;
import com.mrcrayfish.improvedwolves.client.screen.DogBowlScreen;
import com.mrcrayfish.improvedwolves.entity.ai.goal.MoveToDogBowlGoal;
import com.mrcrayfish.improvedwolves.init.ModBlocks;
import com.mrcrayfish.improvedwolves.init.ModContainers;
import com.mrcrayfish.improvedwolves.init.ModTileEntities;
import com.mrcrayfish.improvedwolves.tileentity.DogBowlTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
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
            wolfEntity.goalSelector.addGoal(6, new MoveToDogBowlGoal(wolfEntity));
        }
    }
}
