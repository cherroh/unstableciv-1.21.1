package cherrow.unstableciv.spell;

/**
 * Tweak fragile fishing rod spell behavior here.
 */
public final class FragileRodSpellConfig {
    private FragileRodSpellConfig() {
    }

    /** How long the potion rain lasts. */
    public static final int DURATION_SECONDS = 30;
    public static final int DURATION_TICKS = DURATION_SECONDS * 20;

    /** Horizontal distance from the player that potions can spawn. */
    public static final double SPAWN_RADIUS = 100.0;

    /** World Y coordinate where potions appear before falling. */
    public static final double SPAWN_Y = 255.0;

    /**
     * Random offset applied to X/Z so spawns are not locked to block centers.
     * 0.45 spreads roughly across most of a block.
     */
    public static final double BLOCK_POSITION_JITTER = 0.45;

    /** Ticks between spawn waves. */
    public static final int SPAWN_INTERVAL_TICKS = 1;

    /** Inclusive min/max potions spawned per wave. */
    public static final int MIN_POTIONS_PER_WAVE = 5;
    public static final int MAX_POTIONS_PER_WAVE = 5;
}
