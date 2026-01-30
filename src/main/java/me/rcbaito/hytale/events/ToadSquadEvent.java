package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.rcbaito.hytale.events.Helper.EventHelper;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ToadSquadEvent implements ChaosEvent{

    private ScheduledFuture<?> rainTask;

    @Override
    public String getId() {
        return "toadsquad";
    }

    @Override
    public String getName() { return "TOAD SQUAD"; }

    @Override
    public String getDescription() { return "A squad of hostile toads spawns and hunts players relentlessly."; }

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
        EventHelper.spawnEntity(store, ref, "Toad_Rhino_Magma", 4, 1);
    }
    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {

    }
}
