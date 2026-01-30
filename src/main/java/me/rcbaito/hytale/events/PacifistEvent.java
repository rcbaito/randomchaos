package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.rcbaito.hytale.events.Helper.EventHelper;

public class PacifistEvent implements ChaosEvent {
    @Override
    public String getId() {
        return "pacifist";
    }
    @Override
    public String getName() { return "Pacifist"; }

    @Override
    public String getDescription() { return "Players cannot deal damage. Combat is disabled."; }

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
        applyEffects(store, ref);
    }

    @Override
    public void tick(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        if (!EventHelper.hasDamageComponent(store, ref)) {
            applyEffects(store, ref);
        }
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.setPlayerDamage(store, ref, 1.0f);
    }

    private void applyEffects(Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.setPlayerDamage(store, ref, 0.0f);
    }
}
