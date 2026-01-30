package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.rcbaito.hytale.events.Helper.EventHelper;

public class SuperJumpEvent implements ChaosEvent{
    @Override
    public String getId() {
        return "superjump";
    }
    @Override
    public String getName() { return "Super Jump"; }

    @Override
    public String getDescription() { return "Players can jump extremely high."; }

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
        EventHelper.setPlayerJump(store, ref, 25.0f);
    }

    @Override
    public void tick(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.setPlayerJump(store, ref, 25.0f);
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.setPlayerJump(store, ref, -1f);
    }
}
