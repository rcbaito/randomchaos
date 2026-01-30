package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.Modifier;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.commands.NPCSpawnCommand;
import me.rcbaito.hytale.events.Helper.EventHelper;

import java.util.UUID;

public class GlassCanonEvent implements ChaosEvent{
    @Override
    public String getId() {
        return "glasscanon";
    }
      @Override
    public String getName() { return "Glass Canon"; }

    @Override
    public String getDescription() { return "Players have only 1 HP but deal extremely high damage."; }

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
        EventHelper.modifyStat(store, ref, DefaultEntityStatTypes.getHealth(), "glass_hp", 1.0f, StaticModifier.CalculationType.MULTIPLICATIVE);
    }

    private void applyEffects(Store<EntityStore> store, Ref<EntityStore> ref) {
        EventHelper.setPlayerDamage(store, ref, 200.0f);
        EventHelper.modifyStat(store, ref, DefaultEntityStatTypes.getHealth(), "glass_hp", -0.98f, StaticModifier.CalculationType.MULTIPLICATIVE);
    }
}
