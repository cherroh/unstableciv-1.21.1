package cherrow.unstableciv.item.custom;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class UselessIronOrbitalItem extends OrbitalCannonRodItem {
    public UselessIronOrbitalItem(Settings settings) {
        super(settings);
    }

    @Override
    protected void triggerEffect(ServerWorld world, ServerPlayerEntity player) {
        // Intentionally does nothing; this item is a cosmetic fishing rod.
    }
}
