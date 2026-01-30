package me.rcbaito.hytale.plugin;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import me.rcbaito.hytale.commands.ChaosCommand;
import me.rcbaito.hytale.events.Helper.EventHelper;

import javax.annotation.Nonnull;

public class randomchaos extends JavaPlugin {

    public randomchaos(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        getCommandRegistry().registerCommand(new ChaosCommand());

        try {
            System.out.println("[Chaos] Attempting to register ECS components...");

            EventHelper.DamageMultiplierComponent.TYPE =
                    this.getEntityStoreRegistry().registerComponent(
                            EventHelper.DamageMultiplierComponent.class,
                            EventHelper.DamageMultiplierComponent::new
                    );

            this.getEntityStoreRegistry().registerSystem(new EventHelper.DamageSystem());

            EventHelper.ChaosSpeedComponent.TYPE =
                    this.getEntityStoreRegistry().registerComponent(
                            EventHelper.ChaosSpeedComponent.class,
                            EventHelper.ChaosSpeedComponent::new
                    );
            EventHelper.ChaosJumpComponent.TYPE =
                    this.getEntityStoreRegistry().registerComponent(
                            EventHelper.ChaosJumpComponent.class,
                            EventHelper.ChaosJumpComponent::new
                    );
            System.out.println("[Chaos] SUCCESS: All systems and components loaded!");

        } catch (Exception e) {
            System.out.println("[Chaos] CRITICAL ERROR IN SETUP: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
