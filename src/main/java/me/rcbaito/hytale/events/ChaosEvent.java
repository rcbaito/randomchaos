package me.rcbaito.hytale.events;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref; // Importante: Ref genérico

public interface ChaosEvent {
    String getId();

    String getName();
    String getDescription();

    boolean isEffect();
    int getDuration();

    void start(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref);
    void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref);
    default void tick(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {}
}