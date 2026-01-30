package me.rcbaito.hytale.plugin;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.rcbaito.hytale.events.ChaosEvent;
import me.rcbaito.hytale.events.Helper.EventHelper;
import me.rcbaito.hytale.hud.RandomChaosHUD;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ChaosGameManager {

    public static boolean isRunning = false;
    public static ScheduledFuture<?> activeTask = null;

    public static int chaosTime = 60;
    public static int maxChaosTime = 60;
    public static int timerSpeed = 1;

    public static final List<EventActive> activeEvents = new CopyOnWriteArrayList<>();

    public static final Map<UUID, PlayerRef> affectedPlayers = new ConcurrentHashMap<>();

    public static World currentWorld;
    public static Store<EntityStore> currentStore;

    public static void startLoop() {
        if (isRunning) return;

        isRunning = true;
        chaosTime = maxChaosTime;

        stopAllActiveEvents();
        activeEvents.clear();

        System.out.println("[Chaos] Loop started. Safe thread activated.");

        activeTask = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(() -> {
            if (currentWorld != null) {
                currentWorld.execute(() -> {
                    try {
                        if (chaosTime > 0) {
                            chaosTime -= timerSpeed;
                            if (chaosTime < 0) chaosTime = 0;
                        } else {
                            triggerRandomEvent();
                            chaosTime = maxChaosTime; // Reset timer
                        }

                        processActiveEvents();

                        EventHelper.processMovementTick();

                        updateHUD();

                    } catch (Exception e) {
                        System.out.println("[Chaos] Error inside Loop: " + e.getMessage());
                        e.printStackTrace();
                        stopLoop(); // Safety stop
                    }
                });
            } else {
                System.out.println("[Chaos] World is null! Stopping loop.");
                stopLoop();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static void stopLoop() {
        isRunning = false;
        System.out.println("[Chaos] Stopping loop...");

        if (activeTask != null) {
            try { activeTask.cancel(true); } catch (Exception ignored) {}
            activeTask = null;
        }

        stopAllActiveEvents();
        activeEvents.clear();
        fullResetPlayers();

        clearHUDs();
    }


    public static void triggerRandomEvent() {
        ChaosEvent event = ChaosEventRegistry.getRandomEvent();

        if (event == null) {
            System.out.println("[Chaos] No events recorded!");
            return;
        }

        if (isEventActive(event.getName())) {
            System.out.println("[Chaos] Event '" + event.getName() + "' It's already active! Skipping.");
            return;
        }

        System.out.println("[Chaos] Drawn Event: " + event.getName());

        Runnable onStartLogic = () -> {
            for (PlayerRef pRef : affectedPlayers.values()) {
                if (!isValid(pRef)) continue;
                Player player = getPlayerFromRef(pRef);
                if (player != null) {
                    try {
                        event.start(player, currentWorld, currentStore, pRef.getReference());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Runnable onStopLogic = () -> {
            if (event.isEffect()) {
                for (PlayerRef pRef : affectedPlayers.values()) {
                    if (!isValid(pRef)) continue;
                    Player player = getPlayerFromRef(pRef);
                    if (player != null) {
                        try {
                            event.stop(player, currentWorld, currentStore, pRef.getReference());
                        } catch (Exception ignored) {}
                    }
                }
            }
        };

        startTemporaryEvent(event, onStartLogic, onStopLogic);
    }

    public static void startTemporaryEvent(ChaosEvent event, Runnable onStart, Runnable onStop) {
        if (onStart != null) onStart.run();

        int displayDuration = event.isEffect() ? event.getDuration() : 10;

        activeEvents.add(new EventActive(event, displayDuration, onStop));
        System.out.println("[Chaos] Started: " + event.getName());
    }

    private static void processActiveEvents() {
        for (EventActive active : activeEvents) {
            active.tick();

            if (!active.isFinished()) {
                ChaosEvent event = active.getEvent();

                for (PlayerRef pRef : affectedPlayers.values()) {
                    if (!isValid(pRef)) continue;
                    try {
                        Ref<EntityStore> entityRef = pRef.getReference();
                        if (currentStore != null && currentWorld != null) {
                            Player player = currentStore.getComponent(entityRef, Player.getComponentType());
                            if (player != null) {
                                event.tick(player, currentWorld, currentStore, entityRef);
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }

            if (active.isFinished()) {
                active.stop();
                activeEvents.remove(active);
                System.out.println("[Chaos] Finished: " + active.getEvent().getName());
            }
        }
    }

    private static boolean isEventActive(String eventName) {
        for (EventActive active : activeEvents) {
            if (active.getEvent().getName().equalsIgnoreCase(eventName)) {
                return true;
            }
        }
        return false;
    }

    private static void stopAllActiveEvents() {
        for (EventActive event : activeEvents) {
            try { event.stop(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private static boolean isValid(PlayerRef pRef) {
        return pRef != null && pRef.isValid() && pRef.getReference() != null;
    }

    private static Player getPlayerFromRef(PlayerRef pRef) {
        if (!isValid(pRef)) return null;
        try {
            return pRef.getReference().getStore().getComponent(pRef.getReference(), Player.getComponentType());
        } catch (Exception e) { return null; }
    }

    // HUD & PLAYERS

    public static void fullResetPlayers() {
        if (affectedPlayers.isEmpty() || currentStore == null) return;

        System.out.println("[Chaos] Performing full reset on " + affectedPlayers.size() + " players.");

        for (PlayerRef pRef : affectedPlayers.values()) {
            if (!isValid(pRef)) continue;
            try {
                Ref<EntityStore> ref = pRef.getReference();

                EventHelper.setPlayerSpeed(currentStore, ref, 5.5f);
                EventHelper.setPlayerJump(currentStore, ref, -1f);

                EventHelper.setPlayerDamage(currentStore, ref, 1.0f);

                EventHelper.resetStat(currentStore, ref, DefaultEntityStatTypes.getHealth(), "tank_hp");
                EventHelper.resetStat(currentStore, ref, DefaultEntityStatTypes.getHealth(), "glass_hp");

            } catch (Exception e) {
                System.out.println("Error resetting player: " + e.getMessage());
            }
        }
    }

    public static void addPlayer(PlayerRef player) {
        if (player != null) affectedPlayers.put(player.getUuid(), player);
    }

    public static void removePlayer(PlayerRef player) {
        if (player != null) affectedPlayers.remove(player.getUuid());
    }

    private static void updateHUD() {
        if (affectedPlayers.isEmpty()) return;
        for (PlayerRef pRef : affectedPlayers.values()) {
            Player player = getPlayerFromRef(pRef);
            if (player != null) {
                try {
                    player.getHudManager().setCustomHud(pRef, new RandomChaosHUD(pRef));
                } catch (Exception ignored) {}
            }
        }
    }

    private static void clearHUDs() {
        if (currentStore == null || affectedPlayers.isEmpty()) return;
        for (PlayerRef pRef : affectedPlayers.values()) {
            Player player = getPlayerFromRef(pRef);
            if (player != null) {
                player.getHudManager().setCustomHud(pRef, new CustomUIHud(pRef) {
                    @Override public void build(UICommandBuilder builder) {}
                });
            }
        }
    }
}