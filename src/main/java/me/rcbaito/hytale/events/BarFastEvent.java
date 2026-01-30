package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import me.rcbaito.hytale.plugin.ChaosGameManager;

import java.util.Collections;

public class BarFastEvent implements ChaosEvent{

    @Override
    public String getId() {
        return "2xbar";
    }

    @Override
    public String getName() { return "2X SPEED BAR"; }

    @Override
    public String getDescription() { return "The bar is doubled speed."; }

    @Override
    public int getDuration() {
        return 40;
    }

    @Override
    public boolean isEffect() {
        return true;
    }


    @Override
    public void start(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        ChaosGameManager.timerSpeed = ChaosGameManager.timerSpeed + 1;
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        ChaosGameManager.timerSpeed = ChaosGameManager.timerSpeed - 1;
    }
}
