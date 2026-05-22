package cherrow.unstableciv.spell;

import net.minecraft.entity.player.PlayerEntity;

import java.util.List;
/**
 * Tweak Orbital spell behavior here.
 */
public final class OrbitalSpellConfig {
    private OrbitalSpellConfig() {
    }

    /**
     * Player names allowed to cast Orbital. Comparison is case-insensitive.
     * Add more names to this list as needed.
     */
    public static final List<String> ALLOWED_USERNAMES = List.of("cherrow");

    public static boolean canUse(PlayerEntity player) {
        String username = player.getGameProfile().getName();
        for (String allowed : ALLOWED_USERNAMES) {
            if (allowed.equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /** How long the orbital rain lasts. */
    public static final int DURATION_SECONDS = 30;
    public static final int DURATION_TICKS = DURATION_SECONDS * 20;

    /** Horizontal distance from the player that projectiles can spawn. */
    public static final double SPAWN_RADIUS = 100.0;

    /** World Y coordinate where projectiles appear before falling. */
    public static final double SPAWN_Y = 150.0;

    /**
     * Random offset applied to X/Z so spawns are not locked to block centers.
     * 0.45 spreads roughly across most of a block.
     */
    public static final double BLOCK_POSITION_JITTER = 0.45;

    /** Ticks between spawn waves. */
    public static final int SPAWN_INTERVAL_TICKS = 1;

    /** Inclusive min/max projectiles spawned per wave. */
    public static final int MIN_PROJECTILES_PER_WAVE = 20;
    public static final int MAX_PROJECTILES_PER_WAVE = 20;

    /** Downward speed for ghast-style fireballs (negative Y). */
    public static final double FIREBALL_DOWNWARD_SPEED = 2.0;
}
