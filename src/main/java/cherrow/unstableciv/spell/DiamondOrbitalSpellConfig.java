package cherrow.unstableciv.spell;

/**
 * Tweak Diamond orbital cannon behavior here.
 */
public final class DiamondOrbitalSpellConfig {
    private DiamondOrbitalSpellConfig() {
    }

    /** How far the targeting raycast extends from the player's eyes. */
    public static final double RAYCAST_DISTANCE = 100.0;

    /** Height above the reference point where all TNT spawn before falling. */
    public static final double CENTER_TNT_HEIGHT_ABOVE_REFERENCE = 50.0;

    /** Number of concentric TNT rings around the center TNT. */
    public static final int RING_COUNT = 10;

    /** Lit TNT placed on each ring, evenly spaced around the circle. */
    public static final int TNT_PER_RING = 28;

    /** Diameter of the outermost ring once fully expanded. */
    public static final double OUTERMOST_RING_DIAMETER = 100.0;

    /** Radius of the outermost ring once fully expanded. */
    public static final double OUTERMOST_RING_RADIUS = OUTERMOST_RING_DIAMETER / 2.0;

    /**
     * Radial distance between consecutive rings. Ring {@code n} (1-based) expands to
     * radius {@code n * RING_RADIUS_SPACING}. Defaults to even spacing through the outer radius.
     */
    public static final double RING_RADIUS_SPACING = OUTERMOST_RING_RADIUS / RING_COUNT;

    /** How long ring TNT take to spread horizontally from the reference point. */
    public static final int EXPANSION_DURATION_SECONDS = 2;
    public static final int EXPANSION_DURATION_TICKS = EXPANSION_DURATION_SECONDS * 20;

    /** Fuse length for all spawned TNT once the effect begins. */
    public static final int TNT_FUSE_TICKS = 100;
}
