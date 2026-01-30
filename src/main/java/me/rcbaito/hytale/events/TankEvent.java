package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.rcbaito.hytale.events.Helper.EventHelper;

public class TankEvent implements ChaosEvent{
    @Override
    public String getId() {
        return "tank";
    }
      @Override
    public String getName() { return "Role Tank"; }

    @Override
    public String getDescription() { return "Players gain massive health."; }

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
        EventHelper.modifyStat(store, ref, DefaultEntityStatTypes.getHealth(), "tank_hp", 10.0f, StaticModifier.CalculationType.MULTIPLICATIVE);
    }

    @Override
    public void tick(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.modifyStat(store, ref, DefaultEntityStatTypes.getHealth(), "tank_hp", 1.0f, StaticModifier.CalculationType.MULTIPLICATIVE);
    }
}
