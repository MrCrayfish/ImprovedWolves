package com.mrcrayfish.improvedwolves.init;

import com.mrcrayfish.improvedwolves.Reference;
import com.mrcrayfish.improvedwolves.inventory.container.DogBowlContainer;
import com.mrcrayfish.improvedwolves.inventory.container.CatBowlContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers
{
    private static final List<ContainerType<?>> CONTAINER_TYPES = new ArrayList<>();

    public static final ContainerType<DogBowlContainer> DOG_BOWL = register(Reference.MOD_ID + ":dog_bowl", DogBowlContainer::new);
    public static final ContainerType<CatBowlContainer> Cat_BOWL = register(Reference.MOD_ID + ":cat_bowl", CatBowlContainer::new);

    private static <T extends Container> ContainerType<T> register(String key, ContainerType.IFactory<T> factory)
    {
        ContainerType<T> type = new ContainerType<>(factory);
        type.setRegistryName(key);
        CONTAINER_TYPES.add(type);
        return type;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void registerTypes(final RegistryEvent.Register<ContainerType<?>> event)
    {
        CONTAINER_TYPES.forEach(type -> event.getRegistry().register(type));
        CONTAINER_TYPES.clear();
    }
}
