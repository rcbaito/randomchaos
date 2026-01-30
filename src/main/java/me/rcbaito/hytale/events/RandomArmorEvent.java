package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemArmor;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.filter.ArmorSlotAddFilter;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomArmorEvent implements ChaosEvent{

    private static final Random random = new Random();

    @Override
    public String getId() {
        return "randomarmor";
    }

    @Override
    public String getName() { return "Random Armor"; }

    @Override
    public String getDescription() { return "Equips random armor pieces!"; }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public boolean isEffect() {
        return false;
    }

    private static final List<String> HEADS = Arrays.asList(
            "Armor_Cobalt_Head",
            "Armor_Copper_Head",
            "Armor_Adamantite_Head",
            "Armor_Thorium_Head",
            "Armor_Mithril_Head",
            "Armor_Leather_Soft_Head",
            "Armor_Leather_Raven_Head",
            "Armor_Leather_Medium_Head",
            "Armor_Leather_Light_Head",
            "Armor_Leather_Heavy_Head",
            "Armor_Iron_Head",
            "Armor_Onyxium_Head",
            "Armor_Bronze_Head",
            "Armor_Bronze_Ornate_Head",
            "Armor_Wool_Head",
            "Armor_Wood_Head",
            "Armor_Trork_Head",
            "Armor_Steel_Head",
            "Armor_Steel_Ancient_Head",
            "Armor_Diving_Crude_Head",
            "Armor_Cloth_Silk_Head",
            "Armor_Cloth_Linen_Head",
            "Armor_Cloth_Cotton_Head",
            "Armor_Cloth_Cindercloth_Head",
            "Armor_Prisma_Head"
    );

    private static final List<String> CHESTS = Arrays.asList(
            "Armor_Cobalt_Chest",
            "Armor_Copper_Chest",
            "Armor_Adamantite_Chest",
            "Armor_Thorium_Chest",
            "Armor_Mithril_Chest",
            "Armor_Leather_Soft_Chest",
            "Armor_Leather_Raven_Chest",
            "Armor_Leather_Medium_Chest",
            "Armor_Leather_Light_Chest",
            "Armor_Leather_Heavy_Chest",
            "Armor_Iron_Chest",
            "Armor_Onyxium_Chest",
            "Armor_Bronze_Chest",
            "Armor_Bronze_Ornate_Chest",
            "Armor_Wool_Chest",
            "Armor_Wood_Chest",
            "Armor_Trork_Chest",
            "Armor_Steel_Chest",
            "Armor_Steel_Ancient_Chest",
            "Armor_Diving_Crude_Chest",
            "Armor_Cloth_Silk_Chest",
            "Armor_Cloth_Linen_Chest",
            "Armor_Cloth_Cotton_Chest",
            "Armor_Cloth_Cindercloth_Chest",
            "Armor_Prisma_Chest"
    );

    private static final List<String> HANDS = Arrays.asList(
            "Armor_Cobalt_Hands",
            "Armor_Copper_Hands",
            "Armor_Adamantite_Hands",
            "Armor_Thorium_Hands",
            "Armor_Mithril_Hands",
            "Armor_Leather_Soft_Hands",
            "Armor_Leather_Raven_Hands",
            "Armor_Leather_Medium_Hands",
            "Armor_Leather_Light_Hands",
            "Armor_Leather_Heavy_Hands",
            "Armor_Iron_Hands",
            "Armor_Onyxium_Hands",
            "Armor_Bronze_Hands",
            "Armor_Bronze_Ornate_Hands",
            "Armor_Wool_Hands",
            "Armor_Wood_Hands",
            "Armor_Trork_Hands",
            "Armor_Steel_Hands",
            "Armor_Steel_Ancient_Hands",
            "Armor_Diving_Crude_Hands",
            "Armor_Cloth_Silk_Hands",
            "Armor_Cloth_Linen_Hands",
            "Armor_Cloth_Cotton_Hands",
            "Armor_Cloth_Cindercloth_Hands",
            "Armor_Prisma_Hands"
    );

    private static final List<String> LEGS = Arrays.asList(
            "Armor_Cobalt_Legs",
            "Armor_Copper_Legs",
            "Armor_Adamantite_Legs",
            "Armor_Thorium_Legs",
            "Armor_Mithril_Legs",
            "Armor_Leather_Soft_Legs",
            "Armor_Leather_Raven_Legs",
            "Armor_Leather_Medium_Legs",
            "Armor_Leather_Light_Legs",
            "Armor_Leather_Heavy_Legs",
            "Armor_Iron_Legs",
            "Armor_Onyxium_Legs",
            "Armor_Bronze_Legs",
            "Armor_Bronze_Legs",
            "Armor_Wool_Legs",
            "Armor_Wood_Legs",
            "Armor_Trork_Legs",
            "Armor_Steel_Legs",
            "Armor_Steel_Ancient_Legs",
            "Armor_Diving_Crude_Legs",
            "Armor_Cloth_Silk_Legs",
            "Armor_Cloth_Linen_Legs",
            "Armor_Cloth_Cotton_Legs",
            "Armor_Cloth_Cindercloth_Legs",
            "Armor_Prisma_Legs"
    );



    @Override
    public void start(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        try {
            Inventory inv = player.getInventory();
            if (inv == null) return;

            inv.getArmor().clear();

            equipRandomItem(inv, HEADS);
            equipRandomItem(inv, CHESTS);
            equipRandomItem(inv, LEGS);
            equipRandomItem(inv, HANDS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void equipRandomItem(Inventory inv, List<String> itemList) {
        if (itemList.isEmpty()) return;

        String randomId = itemList.get(random.nextInt(itemList.size()));

        try {
            ItemStack newItem = new ItemStack(randomId, 1);
            Item itemDef = newItem.getItem();


            if (itemDef.getArmor() != null) {
                ItemArmor armorConfig = itemDef.getArmor();
                int correctSlot = armorConfig.getArmorSlot().ordinal();

                inv.getArmor().setItemStackForSlot((short) correctSlot, newItem);
            }
            else {
                inv.getCombinedHotbarFirst().addItemStacks(java.util.Collections.singletonList(newItem));
            }

        } catch (Exception e) {
        }
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {

    }

}
