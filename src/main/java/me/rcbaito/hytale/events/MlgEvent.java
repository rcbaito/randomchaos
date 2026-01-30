package me.rcbaito.hytale.events;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.util.TargetUtil;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport; // Importe o Teleport

import java.util.Collections;

public class MlgEvent implements ChaosEvent {

    @Override
    public String getId() {
        return "mlg";
    }

    @Override
    public String getName() { return "MLG Challenge"; }

    @Override
    public String getDescription() { return "You are teleported high into the sky and given a single Water Bucket."; }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public boolean isEffect() {
        return false;
    }



    @Override
    public void start(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        try {
            Transform current = TargetUtil.getLook(ref, store);
            Vector3d pos = current.getPosition();
            Vector3d newPos = new Vector3d(pos.x, pos.y + 300, pos.z);

            Teleport teleportReq = Teleport.createForPlayer(
                    world,
                    newPos,
                    current.getRotation()
            );

            store.putComponent(
                    ref,
                    Teleport.getComponentType(),
                    teleportReq
            );

            Inventory inv = player.getInventory();
            if (inv != null) {
                inv.getCombinedHotbarFirst().addItemStacks(
                        Collections.singletonList(new ItemStack("*Container_Bucket_State_Filled_Water", 1))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {

    }
}