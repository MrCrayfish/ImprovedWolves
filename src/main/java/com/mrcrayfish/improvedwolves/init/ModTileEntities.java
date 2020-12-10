package com.mrcrayfish.improvedwolves.init;

import com.mrcrayfish.improvedwolves.Reference;
import com.mrcrayfish.improvedwolves.tileentity.DogBowlTileEntity;
import com.mrcrayfish.improvedwolves.tileentity.CatBowlTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTileEntities
{
    private static final List<TileEntityType> TILE_ENTITY_TYPES = new ArrayList<>();

    public static final TileEntityType<DogBowlTileEntity> DOG_BOWL = buildType(Reference.MOD_ID + ":dog_bowl", TileEntityType.Builder.create(DogBowlTileEntity::new, ModBlocks.WHITE_DOG_BOWL, ModBlocks.ORANGE_DOG_BOWL, ModBlocks.MAGENTA_DOG_BOWL, ModBlocks.LIGHT_BLUE_DOG_BOWL, ModBlocks.YELLOW_DOG_BOWL, ModBlocks.LIME_DOG_BOWL, ModBlocks.PINK_DOG_BOWL, ModBlocks.GRAY_DOG_BOWL, ModBlocks.LIGHT_GRAY_DOG_BOWL, ModBlocks.CYAN_DOG_BOWL, ModBlocks.PURPLE_DOG_BOWL, ModBlocks.BLUE_DOG_BOWL, ModBlocks.BROWN_DOG_BOWL, ModBlocks.GREEN_DOG_BOWL, ModBlocks.RED_DOG_BOWL, ModBlocks.BLACK_DOG_BOWL));
    public static final TileEntityType<CatBowlTileEntity> Cat_BOWL = buildType(Reference.MOD_ID + ":cat_bowl", TileEntityType.Builder.create(CatBowlTileEntity::new, ModBlocks.WHITE_Cat_BOWL, ModBlocks.ORANGE_Cat_BOWL, ModBlocks.MAGENTA_Cat_BOWL, ModBlocks.LIGHT_BLUE_Cat_BOWL, ModBlocks.YELLOW_Cat_BOWL, ModBlocks.LIME_Cat_BOWL, ModBlocks.PINK_Cat_BOWL, ModBlocks.GRAY_Cat_BOWL, ModBlocks.LIGHT_GRAY_Cat_BOWL, ModBlocks.CYAN_Cat_BOWL, ModBlocks.PURPLE_Cat_BOWL, ModBlocks.BLUE_Cat_BOWL, ModBlocks.BROWN_Cat_BOWL, ModBlocks.GREEN_Cat_BOWL, ModBlocks.RED_Cat_BOWL, ModBlocks.BLACK_Cat_BOWL));

    private static <T extends TileEntity> TileEntityType<T> buildType(String id, TileEntityType.Builder<T> builder)
    {
        TileEntityType<T> type = builder.build(null); //TODO may not allow null
        type.setRegistryName(id);
        TILE_ENTITY_TYPES.add(type);
        return type;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void registerTypes(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        TILE_ENTITY_TYPES.forEach(type -> event.getRegistry().register(type));
        TILE_ENTITY_TYPES.clear();
    }
}
