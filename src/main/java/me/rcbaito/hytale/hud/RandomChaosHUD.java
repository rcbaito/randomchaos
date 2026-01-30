package me.rcbaito.hytale.hud;



import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import me.rcbaito.hytale.events.ChaosEvent;
import me.rcbaito.hytale.plugin.ChaosGameManager;
import me.rcbaito.hytale.plugin.EventActive;

import javax.annotation.Nonnull;
import java.util.List;

public class RandomChaosHUD extends CustomUIHud {

    public RandomChaosHUD(@Nonnull PlayerRef playerRef) {
        super(playerRef);
    }

    @Override
    public void build(UICommandBuilder builder) {
        builder.append("Hud/ChaosBar.ui");

        int currentTime = ChaosGameManager.chaosTime;
        int maxTime = 60;

        float progressPct = 1.0f - ((float) currentTime / (float) maxTime);
        if (progressPct < 0) progressPct = 0;
        if (progressPct > 1) progressPct = 1;

        int chaosMaxWidth = 1000;
        int chaosWidth = (int) (chaosMaxWidth * progressPct);

        Anchor chaosAnchor = new Anchor();
        chaosAnchor.setWidth(Value.of(chaosWidth));
        chaosAnchor.setHeight(Value.of(20));
        builder.setObject("#ChaosProgressBar.Anchor", chaosAnchor);

        List<EventActive> activeEvents = ChaosGameManager.activeEvents;
        int maxSlots = 6;

        for (int i = 1; i <= maxSlots; i++) {
            String slotID = "#EventTemplate" + i;
            String titleID = "#EventTitle" + i;
            String barID = "#EventProgressBar" + i;

            int index = i - 1;

            if (index < activeEvents.size()) {
                EventActive active = activeEvents.get(index);
                ChaosEvent event = active.getEvent();

                builder.set(slotID + ".Visible", true);

                String texto;
                int larguraBarra;
                int larguraMaximaSlot = 100;

                if (event.isEffect()) {
                    texto = event.getName() + " (" + active.getTimeRemaining() + "s)";

                    float pct = (float) active.getTimeRemaining() / (float) active.getTotalDuration();
                    larguraBarra = (int) (larguraMaximaSlot * pct);
                } else {
                    texto = event.getName();
                    larguraBarra = larguraMaximaSlot;
                }

                builder.set(titleID + ".Text", texto);

                Anchor barAnchor = new Anchor();
                barAnchor.setWidth(Value.of(larguraBarra));
                builder.setObject(barID + ".Anchor", barAnchor);

            } else {
                builder.set(slotID + ".Visible", false);
            }
        }
    }
}