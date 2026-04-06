package cherrow.unstableciv.item.custom;

import cherrow.unstableciv.client.PinkEffectClient;
import net.minecraft.item.Item;
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
        if (world.isClient) {
            PinkEffectClient.toggle();
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

}
