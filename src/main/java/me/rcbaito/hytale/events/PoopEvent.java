package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;

import java.util.Collections;

public class PoopEvent implements ChaosEvent {

    @Override
    public String getId() {
        return "poop";
    }

    @Override
    public String getName() { return "100 POOPS!!!"; }

    @Override
    public String getDescription() { return "You Gain 100 Poops in your Inventory."; }

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
                        Collections.singletonList(new ItemStack("Ingredient_Poop", 100))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {

    }
}