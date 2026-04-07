package cherrow.unstableciv.sound;

import cherrow.unstableciv.Unstableciv;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent SNORT = registerSoundEvent("snort");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(Unstableciv.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        Unstableciv.LOGGER.info("Registering Mod Sounds for " + Unstableciv.MOD_ID);
    }
}
