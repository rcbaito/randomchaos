package me.rcbaito.hytale.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message; // Importante
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.rcbaito.hytale.events.ChaosEvent;
import me.rcbaito.hytale.plugin.ChaosEventRegistry;
import me.rcbaito.hytale.plugin.ChaosGameManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ChaosCommand extends AbstractCommandCollection {

    public ChaosCommand() {
        super("chaos", "Shows all available commands to use");

        addSubCommand(new StartCommand());
        addSubCommand(new StopCommand());
        addSubCommand(new AddCommand());
        addSubCommand(new RemoveCommand());
        addSubCommand(new PlayersCommand());
        addSubCommand(new TestCommand());
        addSubCommand(new EventsCommand());
    }

    // --- SUBCOMAND: START ---
    private static class StartCommand extends AbstractPlayerCommand {
        StartCommand() { super("start", "Starts the Chaos loop"); }

        @Override
        protected void execute(@Nonnull CommandContext ctx, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef sender, @Nonnull World world) {

            if (ChaosGameManager.affectedPlayers.isEmpty()) {
                sender.sendMessage(Message.raw("Add players first: /chaos add <player>").color("#FF5555"));
                return;
            }

            if (ChaosGameManager.isRunning) {
                sender.sendMessage(Message.raw("Chaos is already running!").color("#FF5555"));
            } else {
                ChaosGameManager.currentWorld = world;
                ChaosGameManager.currentStore = store;

                ChaosGameManager.startLoop();
                sender.sendMessage(Message.raw("Chaos Loop STARTED!").color("#55FF55"));
            }
        }
    }

    // --- SUBCOMAND: STOP ---
    private static class StopCommand extends AbstractPlayerCommand {
        StopCommand() { super("stop", "Stop the Chaos loop and resets effects"); }

        @Override
        protected void execute(@Nonnull CommandContext ctx, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef sender, @Nonnull World world) {
            if (!ChaosGameManager.isRunning) {
                sender.sendMessage(Message.raw("Chaos is not running.").color("#FF5555"));
            } else {
                ChaosGameManager.stopLoop();
                sender.sendMessage(Message.raw("Chaos Loop STOPPED and clean effects.").color("#FFFF55"));
            }
        }
    }

    private static class EventsCommand extends AbstractPlayerCommand {
        EventsCommand() { super("events", "List of available events"); }
        @Override protected void execute(@Nonnull CommandContext ctx, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef sender, @Nonnull World world) {
            List<ChaosEvent> list = ChaosEventRegistry.getEvents();
            if (list.isEmpty()) { sender.sendMessage(Message.raw("No events recorded.").color("#FF5555")); return; }

            sender.sendMessage(Message.raw("=== Event List (" + list.size() + ") ===").color("#FFFF55"));
            for (ChaosEvent e : list) {
                sender.sendMessage(Message.raw("• ID: " + e.getId() + " | " + e.getName()).color("#55FF55"));
            }
        }
    }

    private static class TestCommand extends AbstractPlayerCommand {
        private final RequiredArg<String> idArg;
        TestCommand() {
            super("test", "Test an event by ID");
            this.idArg = withRequiredArg("id", "Event ID", ArgTypes.STRING);
        }

        @Override protected void execute(@Nonnull CommandContext ctx, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef sender, @Nonnull World world) {
            String search = idArg.get(ctx).toLowerCase();
            ChaosGameManager.currentWorld = world;
            ChaosGameManager.currentStore = store;
            if (ChaosGameManager.affectedPlayers.isEmpty()) ChaosGameManager.addPlayer(sender);

            ChaosEvent found = null;
            for (ChaosEvent e : ChaosEventRegistry.getEvents()) {
                // Verifica ID exato ou se o nome contem
                if (e.getId().equalsIgnoreCase(search) || e.getName().toLowerCase().contains(search)) {
                    found = e;
                    break;
                }
            }

            if (found != null) {
                sender.sendMessage(Message.raw("Testing: " + found.getName()).color("#55FFFF"));

                final ChaosEvent finalEvent = found;
                ChaosGameManager.startTemporaryEvent(found,
                        () -> { // Start Logic
                            for (PlayerRef pRef : ChaosGameManager.affectedPlayers.values()) {
                                try {
                                    Ref<EntityStore> r = pRef.getReference();
                                    Player p = store.getComponent(r, Player.getComponentType());
                                    if (p != null) finalEvent.start(p, world, store, r);
                                } catch (Exception ignored) {}
                            }
                        },
                        () -> { // Stop Logic
                            if (finalEvent.isEffect()) {
                                for (PlayerRef pRef : ChaosGameManager.affectedPlayers.values()) {
                                    try {
                                        Ref<EntityStore> r = pRef.getReference();
                                        Player p = store.getComponent(r, Player.getComponentType());
                                        if (p != null) finalEvent.stop(p, world, store, r);
                                    } catch (Exception ignored) {}
                                }
                            }
                        }
                );
            } else {
                sender.sendMessage(Message.raw("Event not found: " + search).color("#FF5555"));
            }
        }
    }

    // --- SUBCOMAND: ADD <PLAYER> ---
    private static class AddCommand extends AbstractPlayerCommand {
        private final RequiredArg<PlayerRef> targetArg;

        AddCommand() {
            super("add", "Add player to the chaos");
            this.targetArg = withRequiredArg("player", "Player", ArgTypes.PLAYER_REF);
        }

        @Override
        protected void execute(@Nonnull CommandContext ctx, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef sender, @Nonnull World world) {
            PlayerRef target = targetArg.get(ctx);
            if (target != null) {
                ChaosGameManager.currentWorld = world;
                ChaosGameManager.currentStore = store;
                ChaosGameManager.addPlayer(target);
                sender.sendMessage(Message.raw("Added: " + target.getUsername()).color("#55FF55"));
            } else {
                sender.sendMessage(Message.raw("Player not found.").color("#FF5555"));
            }
        }
    }

    // --- SUBCOMAND: REMOVE <PLAYER> ---
    private static class RemoveCommand extends AbstractPlayerCommand {
        private final RequiredArg<PlayerRef> targetArg;

        RemoveCommand() {
            super("remove", "Remove player from chaos");
            this.targetArg = withRequiredArg("player", "Jogador", ArgTypes.PLAYER_REF);
        }

        @Override
        protected void execute(@Nonnull CommandContext ctx, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef sender, @Nonnull World world) {
            PlayerRef target = targetArg.get(ctx);
            if (target != null) {
                ChaosGameManager.removePlayer(target);
                sender.sendMessage(Message.raw("Removed: " + target.getUsername()).color("#FFFF55"));
            }
        }
    }

    // --- SUBCOMAND: PLAYERS ---
    private static class PlayersCommand extends AbstractPlayerCommand {
        PlayersCommand() { super("players", "List of affected players"); }

        @Override
        protected void execute(@Nonnull CommandContext ctx, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef sender, @Nonnull World world) {
            List<String> names = new ArrayList<>();
            for (PlayerRef p : ChaosGameManager.affectedPlayers.values()) {
                names.add(p.getUsername());
            }
            String list = String.join(", ", names);
            if (list.isEmpty()) list = "Nobody";
            sender.sendMessage(Message.raw("Players in Chaos: " + list).color("#55FFFF"));
        }
    }
}