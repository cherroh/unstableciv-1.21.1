package cherrow.unstableciv.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Fishing rod with 1 durability. Breaks when the player casts the bobber.
 * Additional behavior can be added here later.
 */
public class FragileFishingRodItem extends FishingRodItem {
    public FragileFishingRodItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        boolean retrieving = user.fishHook != null;

        TypedActionResult<ItemStack> result = super.use(world, user, hand);

        if (!world.isClient && !retrieving && !user.getAbilities().creativeMode) {
            stack.damage(1, user, LivingEntity.getSlotForHand(hand));
        }

        return result;
    }
}
