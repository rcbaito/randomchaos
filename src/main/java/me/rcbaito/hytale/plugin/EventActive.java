package me.rcbaito.hytale.plugin;

import me.rcbaito.hytale.events.ChaosEvent;

public class EventActive {
    private final ChaosEvent event;
    private int timeLeft;
    private final int totalDuration;
    private final Runnable onStopCallback;
    private boolean finished = false;

    public EventActive(ChaosEvent event, int duration, Runnable onStopCallback) {
        this.event = event;
        this.timeLeft = duration;
        this.totalDuration = duration;
        this.onStopCallback = onStopCallback;
    }

    public void tick() {
        if (timeLeft > 0) timeLeft--;
        else finished = true;
    }

    public void stop() {
        if (onStopCallback != null) onStopCallback.run();
    }

    public boolean isFinished() { return finished; }
    public ChaosEvent getEvent() { return this.event; }

    public int getTimeRemaining() { return timeLeft; }
    public int getTotalDuration() { return totalDuration; }
}