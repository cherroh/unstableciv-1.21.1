package cherrow.unstableciv.item;
import cherrow.unstableciv.Unstableciv;
import cherrow.unstableciv.item.custom.CopperFishingRodItem;
import cherrow.unstableciv.item.custom.DiamondFishingRodItem;
import cherrow.unstableciv.item.custom.GoldFishingRodItem;
import cherrow.unstableciv.item.custom.HighItem;
import cherrow.unstableciv.item.custom.IronFishingRodItem;
import cherrow.unstableciv.item.custom.OrbitalItem;
import cherrow.unstableciv.item.custom.TotemOfRespawningItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;


public class ModItems {
    public static final Item WHITE_STUFF = registerItem("white_stuff", new HighItem(new Item.Settings()));
    public static final Item BLUE_STUFF = registerItem("blue_stuff", new HighItem(new Item.Settings()));
    public static final Item ORBITAL = registerItem(
            "orbital",
            new OrbitalItem(new Item.Settings().maxDamage(1)));
    public static final Item COPPER_FISHING_ROD = registerItem(
            "copper_fishing_rod",
            new CopperFishingRodItem(new Item.Settings().maxDamage(1)));
    public static final Item IRON_FISHING_ROD = registerItem(
            "iron_fishing_rod",
            new IronFishingRodItem(new Item.Settings().maxDamage(1)));
    public static final Item GOLD_FISHING_ROD = registerItem(
            "gold_fishing_rod",
            new GoldFishingRodItem(new Item.Settings().maxDamage(1)));
    public static final Item DIAMOND_FISHING_ROD = registerItem(
            "diamond_fishing_rod",
            new DiamondFishingRodItem(new Item.Settings().maxDamage(1)));
    public static final Item TOTEM_OF_RESPAWNING = registerItem(
            "totem_of_respawning",
            new TotemOfRespawningItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Unstableciv.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Unstableciv.LOGGER.info("Registering Mod Items for " + Unstableciv.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(WHITE_STUFF);
            entries.add(BLUE_STUFF);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(ORBITAL);
            entries.add(COPPER_FISHING_ROD);
            entries.add(IRON_FISHING_ROD);
            entries.add(GOLD_FISHING_ROD);
            entries.add(DIAMOND_FISHING_ROD);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(TOTEM_OF_RESPAWNING);
        });
    }
}
