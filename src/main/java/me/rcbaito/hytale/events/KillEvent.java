package me.rcbaito.hytale.events;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;

// Imports que encontramos no KillCommand.txt [cite: 829]
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;

public class KillEvent implements ChaosEvent {

    @Override
    public String getId() {
        return "kill";
    }

    @Override
    public String getName() { return "Instant Death"; }

    @Override
    public String getDescription() { return "All players instantly die."; }

    @Override
    public boolean isEffect() {
        return false;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public void start(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        try {

            EntityStatMap stats = store.getComponent(ref, EntityStatMap.getComponentType());

            if (stats != null) {

                int healthIndex = EntityStatType.getAssetMap().getIndex("health");

                stats.setStatValue(healthIndex, 0.0f);

            } else {
            }

        } catch (Exception e) {
        }
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {

    }
}
