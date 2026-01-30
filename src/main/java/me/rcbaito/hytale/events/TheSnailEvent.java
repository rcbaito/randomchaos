package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.rcbaito.hytale.events.Helper.EventHelper;

public class TheSnailEvent implements ChaosEvent{
    @Override
    public String getId() {
        return "snail";
    }
    @Override
    public String getName() { return "The Snail"; }

    @Override
    public String getDescription() { return "You are like a snail, slow and unable to jump."; }

    @Override
    public int getDuration() {
        return 30;
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public void start(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.setPlayerSpeed(store, ref, 2.0f);
        EventHelper.setPlayerJump(store, ref, 0.0f);
    }

    @Override
    public void tick(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.setPlayerSpeed(store, ref, 2.0f);
        EventHelper.setPlayerJump(store, ref, 0.0f);
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.setPlayerSpeed(store, ref, 5.5f);
        EventHelper.setPlayerJump(store, ref, -1f);
    }
}
