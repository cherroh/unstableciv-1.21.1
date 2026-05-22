package cherrow.unstableciv.spell;

/**
 * Tweak Orbital spell behavior here.
 */
public final class OrbitalSpellConfig {
    private OrbitalSpellConfig() {
    }

    /** How long the orbital rain lasts. */
    public static final int DURATION_SECONDS = 30;
    public static final int DURATION_TICKS = DURATION_SECONDS * 20;

    /** Horizontal distance from the player that projectiles can spawn. */
    public static final double SPAWN_RADIUS = 100.0;

    /** World Y coordinate where projectiles appear before falling. */
    public static final double SPAWN_Y = 255.0;

    /**
     * Random offset applied to X/Z so spawns are not locked to block centers.
     * 0.45 spreads roughly across most of a block.
     */
    public static final double BLOCK_POSITION_JITTER = 0.45;

    /** Ticks between spawn waves. */
    public static final int SPAWN_INTERVAL_TICKS = 1;

    /** Inclusive min/max projectiles spawned per wave. */
    public static final int MIN_PROJECTILES_PER_WAVE = 5;
    public static final int MAX_PROJECTILES_PER_WAVE = 5;

    /** Downward speed for ghast-style fireballs (negative Y). */
    public static final double FIREBALL_DOWNWARD_SPEED = 1.0;
}
