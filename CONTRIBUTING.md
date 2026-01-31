# Contributing to Random Chaos Mod

First off, thank you for considering contributing to the Random Chaos Mod! We welcome all developers who want to add more chaos to the Hytale world.

To maintain code quality and stability, please follow the guidelines below when creating new events.

## 🛠️ How to Create a New Event

Adding a new Chaos Event is simple, but it must follow a strict pattern to ensure compatibility and scalability.

### 1. Create the Event Class
Create a new class inside the `me.rcbaito.hytale.events` package. Your class must implement the `ChaosEvent` interface.

**Naming Convention:** The class name should end with `Event` (e.g., `MoonGravityEvent`, `ExplodingChickensEvent`).

### 2. Implementation Template
Use this template to get started. It includes all necessary methods and imports.

```java
package me.rcbaito.hytale.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.rcbaito.hytale.events.Helper.EventHelper;

public class MyNewEvent implements ChaosEvent {

    @Override public String getId() { return "my_new_event"; } // Unique ID (snake_case)
    @Override public String getName() { return "My New Event"; } // Display Name
    @Override public String getDescription() { return "Short description of what happens."; }
    @Override public int getDuration() { return 30; } // Duration in seconds
    @Override public boolean isEffect() { return true; } // True if it lasts over time, False if instant

    // HUD Colors (Hex Codes)
    @Override public String getFirstColor() { return "#FF5555"; } 
    @Override public String getSecondColor() { return "#FFFFFF"; }

    @Override
    public void start(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        player.sendMessage("§cMy Event Started!");
        // Your logic here
    }

    @Override
    public void tick(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        // Called every tick (only if isEffect() is true)
    }

    @Override
    public void stop(Player player, World world, Store<EntityStore> store, Ref<EntityStore> ref) {
        player.sendMessage("§aMy Event Ended.");
        // CLEANUP IS MANDATORY! Reset stats, speed, jump, etc.
    }
}
```
### 3. Key Rules (The "Golden Standard")
To keep the mod stable and clean, you must adhere to these rules:

Single Class Rule: Keep the event logic contained within its own class. Do not spread logic across multiple files unless absolutely necessary.

Use EventHelper: Do NOT manually manipulate ECS components (like MovementManager or raw stats) inside the event class. We have a centralized helper for that to handle networking and packets.

❌ Bad: movement.getSettings().baseSpeed = 10;

✅ Good: EventHelper.setPlayerSpeed(store, ref, 10.0f);

✅ Good: EventHelper.setPlayerJump(store, ref, 20.0f);

Scalability: If you need a new utility function (e.g., "Change Gravity" or "Spawn Lightning"), add it to EventHelper.java instead of writing it inside the event. The method must be reusable by other future events.

Cleanup: If your event changes a player's state (speed, health, jump force), you MUST reset it in the stop() method using the helper methods (usually passing -1 or default values).

### 4. Registering the Event
After creating your class, don't forget to register it in ChaosEventRegistry.java:

```Java
events.add(new MyNewEvent());
```
🐛 Found a Bug?
If you find a bug in the source code, you can help us by submitting an issue to our GitHub Repository. Even better, you can submit a Pull Request with a fix.

💡 Feature Requests
You can request a new feature by submitting an issue. If you would like to implement a new feature yourself, please consider the guidelines above.
