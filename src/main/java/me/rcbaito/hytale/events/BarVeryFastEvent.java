package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.rcbaito.hytale.plugin.ChaosGameManager;

public class BarVeryFastEvent implements ChaosEvent{

    @Override
    public String getId() {
        return "5xbar";
    }

    @Override
    public String getName() { return "5X SPEED BAR"; }

    @Override
    public String getDescription() { return "The bar is multiplied by five speed."; }

    @Override
    public int getDuration() {
        return 40;
    }

    @Override
    public boolean isEffect() {
        return true;
    }



    @Override
    public void start(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        ChaosGameManager.timerSpeed = ChaosGameManager.timerSpeed + 4;
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        ChaosGameManager.timerSpeed = ChaosGameManager.timerSpeed - 4;
    }
}
