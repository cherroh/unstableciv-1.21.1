package cherrow.unstableciv;

import cherrow.unstableciv.item.ModItems;
import cherrow.unstableciv.sound.ModSounds;
import cherrow.unstableciv.spell.DiamondOrbitalStrike;
import cherrow.unstableciv.spell.OrbitalThingy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unstableciv implements ModInitializer {
	public static final String MOD_ID = "unstableciv";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		ModItems.registerModItems();
		ModSounds.registerSounds();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			OrbitalThingy.tick(server);
			DiamondOrbitalStrike.tick(server);
		});

		System.out.println("Please work I beg please please please");
	}
}