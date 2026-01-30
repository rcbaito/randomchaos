package me.rcbaito.hytale.events.Helper;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.OrderPriority;
import com.hypixel.hytale.component.dependency.SystemGroupDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.component.system.tick.TickingSystem;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.CalculationType;
import com.hypixel.hytale.protocol.EntityStatEffects;
import com.hypixel.hytale.protocol.packets.entities.ChangeVelocity;
import com.hypixel.hytale.server.core.command.commands.player.stats.PlayerStatsAddCommand;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSettings;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.Modifier;
import com.hypixel.hytale.server.core.modules.entitystats.modifier.StaticModifier;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.npc.INonPlayerCharacter;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.hypixel.hytale.server.npc.NPCPlugin;
import it.unimi.dsi.fastutil.Pair;
import me.rcbaito.hytale.plugin.ChaosGameManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EventHelper {

    public static class ChaosSpeedComponent implements Component<EntityStore> {
        public static ComponentType<EntityStore, ChaosSpeedComponent> TYPE;
        public float targetSpeed;
        public ChaosSpeedComponent() { this.targetSpeed = 5.5f; }
        public ChaosSpeedComponent(float speed) { this.targetSpeed = speed; }
        @Nullable @Override public Component<EntityStore> clone() { return new ChaosSpeedComponent(this.targetSpeed); }
    }

    public static class ChaosJumpComponent implements Component<EntityStore> {
        public static ComponentType<EntityStore, ChaosJumpComponent> TYPE;
        public float targetJump;
        public ChaosJumpComponent() { this.targetJump = 8.0f; } // 8.0f é um valor base aproximado
        public ChaosJumpComponent(float jump) { this.targetJump = jump; }
        @Nullable @Override public Component<EntityStore> clone() { return new ChaosJumpComponent(this.targetJump); }
    }

    public static class DamageMultiplierComponent implements Component<EntityStore> {
        public static ComponentType<EntityStore, DamageMultiplierComponent> TYPE;
        public float multiplier;
        public DamageMultiplierComponent() { this.multiplier = 1.0f; }
        public DamageMultiplierComponent(float multiplier) { this.multiplier = multiplier; }
        @Nullable @Override public Component<EntityStore> clone() { return new DamageMultiplierComponent(this.multiplier); }
    }

    public static void processMovementTick() {
        if (ChaosSpeedComponent.TYPE == null || ChaosJumpComponent.TYPE == null) return;

        for (PlayerRef pRef : ChaosGameManager.affectedPlayers.values()) {
            if (pRef == null || !pRef.isValid()) continue;

            try {
                Ref<EntityStore> ref = pRef.getReference();
                Store<EntityStore> store = ref.getStore();

                ComponentType<EntityStore, MovementManager> moveType = EntityModule.get().getMovementManagerComponentType();
                MovementManager movement = store.getComponent(ref, moveType);

                if (movement == null) continue;

                boolean needsUpdate = false;

                ChaosSpeedComponent mySpeed = store.getComponent(ref, ChaosSpeedComponent.TYPE);
                if (mySpeed != null) {
                    if (movement.getSettings().baseSpeed != mySpeed.targetSpeed) {
                        movement.getSettings().baseSpeed = mySpeed.targetSpeed;
                        needsUpdate = true;
                    }
                }

                ChaosJumpComponent myJump = store.getComponent(ref, ChaosJumpComponent.TYPE);
                if (myJump != null) {
                    if (movement.getSettings().jumpForce != myJump.targetJump) {
                        movement.getSettings().jumpForce = myJump.targetJump;
                        needsUpdate = true;
                    }
                }

                if (needsUpdate) {
                    if (pRef.getPacketHandler() != null) {
                        movement.update(pRef.getPacketHandler());
                    }
                }

            } catch (Exception ignored) {}
        }
    }

    // --- SETTERS ---

    public static void setPlayerSpeed(Store<EntityStore> store, Ref<EntityStore> ref, float speed) {
        if (ChaosSpeedComponent.TYPE == null) return;

        if (store.getComponent(ref, ChaosSpeedComponent.TYPE) != null) {
            store.removeComponent(ref, ChaosSpeedComponent.TYPE);
        }

        if (speed == 5.5f) {
            forceResetMovement(store, ref, true, false);
        } else {
            store.addComponent(ref, ChaosSpeedComponent.TYPE, new ChaosSpeedComponent(speed));
        }
    }

    public static void setPlayerJump(Store<EntityStore> store, Ref<EntityStore> ref, float jumpForce) {
        if (ChaosJumpComponent.TYPE == null) return;

        if (store.getComponent(ref, ChaosJumpComponent.TYPE) != null) {
            store.removeComponent(ref, ChaosJumpComponent.TYPE);
        }

        if (jumpForce == -1f) {
            forceResetMovement(store, ref, false, true);
        } else {
            store.addComponent(ref, ChaosJumpComponent.TYPE, new ChaosJumpComponent(jumpForce));
        }
    }

    private static void forceResetMovement(Store<EntityStore> store, Ref<EntityStore> ref, boolean resetSpeed, boolean resetJump) {
        try {
            ComponentType<EntityStore, MovementManager> moveType = EntityModule.get().getMovementManagerComponentType();
            MovementManager movement = store.getComponent(ref, moveType);
            if (movement == null) return;

            if (resetSpeed) movement.getSettings().baseSpeed = 5.5f;
            if (resetJump) movement.getSettings().jumpForce = 9.0f;

            for (PlayerRef pRef : ChaosGameManager.affectedPlayers.values()) {
                if (pRef.getReference().equals(ref) && pRef.getPacketHandler() != null) {
                    movement.update(pRef.getPacketHandler());
                    break;
                }
            }
        } catch (Exception ignored) {}
    }



    public static class DamageSystem extends DamageEventSystem {
        public DamageSystem() { super(); }

        @Override
        public void handle(int i, @Nonnull ArchetypeChunk<EntityStore> chunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> cb, @Nonnull Damage damage) {
            Object source = damage.getSource();
            if (source == null) return;
            Ref<EntityStore> attacker = null;

            if (source instanceof Ref) attacker = (Ref<EntityStore>) source;
            else {
                String[] methods = {"getSourceRef", "getRef", "getEntityReference", "getOwner", "getEntity"};
                for (String mName : methods) {
                    try {
                        Method m = source.getClass().getMethod(mName);
                        Object result = m.invoke(source);
                        if (result instanceof Ref) { attacker = (Ref<EntityStore>) result; break; }
                    } catch (Exception ignored) {}
                }
            }

            if (attacker != null && attacker.isValid() && DamageMultiplierComponent.TYPE != null) {
                DamageMultiplierComponent comp = store.getComponent(attacker, DamageMultiplierComponent.TYPE);
                if (comp != null && comp.multiplier != 1.0f) {
                    damage.setAmount(damage.getAmount() * comp.multiplier);
                }
            }
        }

        @Nonnull @Override public Query<EntityStore> getQuery() { return Query.any(); } // Query simples
        @Nullable @Override public SystemGroup<EntityStore> getGroup() { return DamageModule.get().getFilterDamageGroup(); }
        @Nonnull @Override public Set<Dependency<EntityStore>> getDependencies() { return Collections.emptySet(); }
    }

    public static boolean hasDamageComponent(Store<EntityStore> store, Ref<EntityStore> ref) {
        if (DamageMultiplierComponent.TYPE == null) return false;
        return store.getComponent(ref, DamageMultiplierComponent.TYPE) != null;
    }

    public static void setPlayerDamage(Store<EntityStore> store, Ref<EntityStore> ref, float multiplier) {
        if (DamageMultiplierComponent.TYPE == null) return;
        if (store.getComponent(ref, DamageMultiplierComponent.TYPE) != null) store.removeComponent(ref, DamageMultiplierComponent.TYPE);
        if (multiplier != 1.0f) store.addComponent(ref, DamageMultiplierComponent.TYPE, new DamageMultiplierComponent(multiplier));
    }


    public static void modifyStat(Store<EntityStore> store, Ref<EntityStore> ref, Object rawStat, String key, float desiredMultiplier, StaticModifier.CalculationType type) {
        try {
            EntityStatMap stats = store.getComponent(ref, EntityStatMap.getComponentType());
            if (stats != null) {
                int statId = getStatId(rawStat);
                if (statId == -1) return;

                stats.removeModifier(statId, key);

                float fixedValue = desiredMultiplier;
                stats.putModifier(statId, key, new StaticModifier(Modifier.ModifierTarget.MAX, type, fixedValue));
                stats.maximizeStatValue(EntityStatMap.Predictable.ALL,statId);

            }
        } catch (Exception e) { System.out.println("Error modifyStat: " + e.getMessage()); }
    }

    public static void resetStat(Store<EntityStore> store, Ref<EntityStore> ref, Object rawStat, String key) {
        try {
            EntityStatMap stats = (EntityStatMap) store.getComponent(ref, EntityStatMap.getComponentType());
            if (stats != null) {
                int statId = getStatId(rawStat);
                if (statId == -1) return;

                stats.removeModifier(statId, key);

                if (statId == getStatId(DefaultEntityStatTypes.getHealth())) {
                    stats.maximizeStatValue(statId);
                }

                // 3. FORCE SYNC (Clone)
                EntityStatMap syncedStats = (EntityStatMap) stats.clone();
                store.putComponent(ref, EntityStatMap.getComponentType(), syncedStats);
            }
        } catch (Exception e) { System.out.println("Error resetStat: " + e.getMessage()); }
    }

    private static int getStatId(Object statObj) {
        if (statObj instanceof Integer) return (int) statObj;
        try { return (int) statObj.getClass().getMethod("getId").invoke(statObj); }
        catch (Exception e) {
            try { return (int) statObj.getClass().getMethod("ordinal").invoke(statObj); }
            catch (Exception ex) { return -1; }
        }
    }

    public static void spawnEntity(Store<EntityStore> store, Ref<EntityStore> ref, String entityName, int count, double yOffset) {
        try {
            Transform transform = TargetUtil.getLook(ref, store);
            Vector3d centerPos = transform.getPosition();

            for (int i = 0; i < count; i++) {
                double offsetX = (count > 1) ? (Math.random() * 4) - 2 : 0;
                double offsetZ = (count > 1) ? (Math.random() * 4) - 2 : 0;
                Vector3d spawnPos = new Vector3d(centerPos.x + offsetX, centerPos.y + yOffset, centerPos.z + offsetZ);
                Vector3f rotation = new Vector3f(0, 0, 0);

                Pair<Ref<EntityStore>, INonPlayerCharacter> result = NPCPlugin.get().spawnNPC(store, entityName, null, spawnPos, rotation);
                if (result == null) System.out.println("Fail to spawn NPC " + (i+1));
            }
        } catch (Exception e) {
            System.out.println("Error spawnEntity: " + e.getMessage());
        }
    }
}
