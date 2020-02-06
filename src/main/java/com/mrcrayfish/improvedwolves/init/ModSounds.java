package com.mrcrayfish.improvedwolves.init;

import com.mrcrayfish.improvedwolves.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSounds
{
    private static final List<SoundEvent> SOUNDS = new ArrayList<>();

    public static final SoundEvent ENTITY_PLAYER_WHISTLE = register(Reference.MOD_ID + ":entity.player.whistle");

    private static SoundEvent register(String name)
    {
        SoundEvent event = new SoundEvent(new ResourceLocation(name));
        event.setRegistryName(name);
        SOUNDS.add(event);
        return event;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void registerSounds(final RegistryEvent.Register<SoundEvent> event)
    {
        SOUNDS.forEach(soundEvent -> event.getRegistry().register(soundEvent));
        SOUNDS.clear();
    }
}
