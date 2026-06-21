package cherrow.unstableciv.item.custom;

import net.minecraft.block.Blocks;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class DiamondFishingRodItem extends OrbitalCannonRodItem {
    public DiamondFishingRodItem(Settings settings) {
        super(settings);
    }

    @Override
    protected void triggerEffect(ServerWorld world, ServerPlayerEntity player) {
        Vec3d pos = getSpawnPos(player);
        BlockPos blockPos = BlockPos.ofFloored(pos.x, pos.y, pos.z);
        FallingBlockEntity anvil = FallingBlockEntity.spawnFromBlock(
                world,
                blockPos,
                Blocks.ANVIL.getDefaultState()
        );
        anvil.setPosition(pos.x, pos.y, pos.z);
    }
}
