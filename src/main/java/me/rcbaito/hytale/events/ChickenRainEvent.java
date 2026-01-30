package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.rcbaito.hytale.events.Helper.EventHelper;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ChickenRainEvent implements ChaosEvent{
    @Override
    public String getId() {
        return "chickenrain";
    }

    @Override
    public String getName() { return "Chicken Rain"; }

    @Override
    public String getDescription() { return "Chickens fall from the sky nonstop."; }

    @Override
    public int getDuration() {
        return 15;
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public void start(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
    }

    @Override
    public void tick(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.spawnEntity(store, ref, "Chicken", 1, 100.0);
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {

    }
}
