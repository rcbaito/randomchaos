package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Collections;

public class RifleEvent implements ChaosEvent {

    @Override
    public String getId() {
        return "rifle";
    }

    @Override
    public String getName() { return "RIFLE"; }

    @Override
    public String getDescription() { return "Players receive rifles."; }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public boolean isEffect() {
        return false;
    }



    @Override
    public void start(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        try {
            Inventory inv = player.getInventory();
            if (inv != null) {
                inv.getCombinedHotbarFirst().addItemStacks(
                        Collections.singletonList(new ItemStack("Weapon_Assault_Rifle", 1))
                );
                inv.getCombinedHotbarFirst().addItemStacks(Collections.singletonList(new ItemStack("Weapon_Arrow_Crude", 50)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {

    }
}