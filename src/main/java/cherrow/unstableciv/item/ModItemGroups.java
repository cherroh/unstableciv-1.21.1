package cherrow.unstableciv.item;

import cherrow.unstableciv.Unstableciv;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> UNSTABLE_CIVILIZATION_KEY = RegistryKey.of(
            RegistryKeys.ITEM_GROUP,
            Identifier.of(Unstableciv.MOD_ID, "unstable_civilization")
    );

    public static void registerItemGroups() {
        Registry.register(
                Registries.ITEM_GROUP,
                UNSTABLE_CIVILIZATION_KEY,
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(ModItems.DIAMOND_FISHING_ROD))
                        .displayName(Text.translatable("itemGroup.unstableciv.unstable_civilization"))
                        .entries((displayContext, entries) -> {
                            entries.add(ModItems.WHITE_STUFF);
                            entries.add(ModItems.BLUE_STUFF);
                            entries.add(ModItems.ORBITAL);
                            entries.add(ModItems.COPPER_FISHING_ROD);
                            entries.add(ModItems.IRON_FISHING_ROD);
                            entries.add(ModItems.GOLD_FISHING_ROD);
                            entries.add(ModItems.DIAMOND_FISHING_ROD);
                            entries.add(ModItems.TOTEM_OF_RESPAWNING);
                        })
                        .build()
        );

        Unstableciv.LOGGER.info("Registering Item Groups for " + Unstableciv.MOD_ID);
    }
}
