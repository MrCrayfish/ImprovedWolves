package com.mrcrayfish.improvedwolves.init;

import com.mojang.datafixers.Dynamic;
import com.mrcrayfish.improvedwolves.Reference;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.math.GlobalPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModMemoryModuleTypes
{
    private static final List<MemoryModuleType<?>> MODULES = new ArrayList<>();

    public static final MemoryModuleType<GlobalPos> CHEST = register(Reference.MOD_ID + ":chest", GlobalPos::deserialize);

    private static <U> MemoryModuleType<U> register(String key, Function<Dynamic<?>, U> function)
    {
        MemoryModuleType<U> module = new MemoryModuleType<>(Optional.of(function));
        module.setRegistryName(key);
        MODULES.add(module);
        return module;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void registerTypes(final RegistryEvent.Register<MemoryModuleType<?>> event)
    {
        MODULES.forEach(type -> event.getRegistry().register(type));
        MODULES.clear();
    }
}
