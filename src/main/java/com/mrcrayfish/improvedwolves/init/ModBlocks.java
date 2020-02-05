package com.mrcrayfish.improvedwolves.init;

import com.mrcrayfish.improvedwolves.ImprovedWolves;
import com.mrcrayfish.improvedwolves.Reference;
import com.mrcrayfish.improvedwolves.block.DogBowlBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks
{
    private static final List<Block> BLOCKS = new ArrayList<>();
    private static final List<Item> ITEMS = new ArrayList<>();

    public static final Block DOG_BOWL = register(new DogBowlBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5F)).setRegistryName(Reference.MOD_ID, "dog_bowl"));

    private static Block register(Block block)
    {
        return register(block, block1 -> new BlockItem(block1, new Item.Properties().group(ImprovedWolves.GROUP)));
    }

    private static Block register(Block block, @Nullable Function<Block, BlockItem> supplier)
    {
        if(block.getRegistryName() == null)
        {
            throw new IllegalArgumentException("A block being registered does not have a registry name and could be registered.");
        }

        BLOCKS.add(block);
        if(supplier != null)
        {
            BlockItem item = supplier.apply(block);
            item.setRegistryName(block.getRegistryName());
            ITEMS.add(item);
        }
        return block;
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event)
    {
        BLOCKS.forEach(block -> event.getRegistry().register(block));
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event)
    {
        ITEMS.forEach(block -> event.getRegistry().register(block));
    }
}
