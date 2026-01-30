package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class GamemodeCreativeEvent implements ChaosEvent{

    @Override
    public String getId() {
        return "creative";
    }

    @Override
    public String getName() { return "/Creative"; }

    @Override
    public String getDescription() { return "Players enter Creative Mode."; }

    @Override
    public int getDuration() {
        return 10;
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public void start(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        player.setGameMode(ref, GameMode.Creative,store);
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        player.setGameMode(ref ,GameMode.Adventure, store);
    }
}
