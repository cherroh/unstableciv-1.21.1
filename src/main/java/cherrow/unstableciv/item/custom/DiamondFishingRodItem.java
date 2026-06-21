package cherrow.unstableciv.item.custom;

import cherrow.unstableciv.spell.DiamondOrbitalStrike;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class DiamondFishingRodItem extends OrbitalCannonRodItem {
    public DiamondFishingRodItem(Settings settings) {
        super(settings);
    }

    @Override
    protected void triggerEffect(ServerWorld world, ServerPlayerEntity player) {
        DiamondOrbitalStrike.trigger(world, player);
    }
}
