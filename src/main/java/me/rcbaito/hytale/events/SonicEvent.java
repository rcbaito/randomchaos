package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.rcbaito.hytale.events.Helper.EventHelper;

public class SonicEvent implements ChaosEvent{
    @Override
    public String getId() {
        return "sonic";
    }
    @Override
    public String getName() { return "Sonic Boots"; }

    @Override
    public String getDescription() { return "Super speed is enabled, allowing players to move at Sonic-like velocity."; }

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
        EventHelper.setPlayerSpeed(store, ref, 50.5f);
    }

    @Override
    public void tick(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.setPlayerSpeed(store, ref, 50.5f);
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.setPlayerSpeed(store, ref, 5.5f);
    }
}
