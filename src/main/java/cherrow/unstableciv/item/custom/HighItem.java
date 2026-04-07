package cherrow.unstableciv.item.custom;

import cherrow.unstableciv.client.PinkEffectClient;
import cherrow.unstableciv.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.util.TypedActionResult;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;

public class HighItem extends Item {
    public HighItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        world.playSound(
                null, // null = no specific listener (broadcast to nearby players)
                user.getX(),
                user.getY(),
                user.getZ(),
                ModSounds.SNORT,
                user.getSoundCategory(),
                1.0F, // volume
                1.0F // pitch
        );
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 40;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            if (user instanceof PlayerEntity player) {

                // Apply 1 second cooldown (20 ticks)
                player.getItemCooldownManager().set(this, 20);

                // Only consume in survival
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
        } else {
            PinkEffectClient.toggle();
        }
        return stack;
    }
}
