package cherrow.unstableciv.item;
import cherrow.unstableciv.Unstableciv;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModItems {
    public static final Item WHITE_STUFF = registerItem("white_stuff", new Item(new Item.Settings()));
    public static final Item BLUE_STUFF = registerItem("blue_stuff", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Unstableciv.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Unstableciv.LOGGER.info("Registering Mod Items for " + Unstableciv.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(WHITE_STUFF);
            entries.add(BLUE_STUFF);
        });
    }
}
