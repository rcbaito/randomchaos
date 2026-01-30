package me.rcbaito.hytale.plugin;

import me.rcbaito.hytale.events.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChaosEventRegistry {

    private static final List<ChaosEvent> events = new ArrayList<>();
    private static final Random random = new Random();

    static {
        events.add(new KillEvent());
        events.add(new MlgEvent());
        events.add(new BarFastEvent());
        events.add(new GamemodeCreativeEvent());
        events.add(new PoopEvent());
        // UPDATE 1.1.0
        events.add(new BarVeryFastEvent());
        events.add(new ChickenRainEvent());
        events.add(new ToadSquadEvent());
        events.add(new GlassCanonEvent());
        events.add(new TankEvent());
        events.add(new RifleEvent());
        events.add(new PacifistEvent());
        events.add(new SonicEvent());
        events.add(new TheSnailEvent());
        events.add(new SuperJumpEvent());
        events.add(new CementShoesEvent());
        events.add(new RandomArmorEvent());

    }

    public static void register(ChaosEvent event) {
        events.add(event);
    }

    public static ChaosEvent getRandomEvent() {
        if (events.isEmpty()) return null;
        return events.get(random.nextInt(events.size()));
    }

    public static List<ChaosEvent> getEvents() {
        return events;
    }
}