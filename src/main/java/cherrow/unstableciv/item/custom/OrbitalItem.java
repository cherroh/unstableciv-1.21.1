package cherrow.unstableciv.item.custom;

import cherrow.unstableciv.spell.OrbitalRainSpell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Fishing rod with 1 durability in survival. Casting triggers an orbital strike.
 * Works indefinitely in creative.
 */
public class OrbitalItem extends FishingRodItem {
    public OrbitalItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        boolean retrieving = user.fishHook != null;

        TypedActionResult<ItemStack> result = super.use(world, user, hand);

        if (!world.isClient && !retrieving) {
            if (user instanceof ServerPlayerEntity serverPlayer && world instanceof ServerWorld serverWorld) {
                OrbitalRainSpell.trigger(serverWorld, serverPlayer);
            }

            if (!user.getAbilities().creativeMode) {
                stack.damage(1, user, LivingEntity.getSlotForHand(hand));
            }
        }

        return result;
    }
}
