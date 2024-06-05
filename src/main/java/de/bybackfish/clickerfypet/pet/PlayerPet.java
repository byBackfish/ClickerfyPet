package de.bybackfish.clickerfymuseum.pet;

import com.destroystokyo.paper.ParticleBuilder;
import de.bybackfish.clickerfymuseum.config.PetConfig;
import io.papermc.paper.entity.TeleportFlag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;


public class PlayerPet {

    private final PetConfig pet;
    private final int level;

    ItemDisplay itemDisplay;
    Slime slime;
    SmoothTeleporter teleporter;

    private Location lastPlayerLocation;
    private double t = 0;  // Used for wobble effect

    public PlayerPet(PetConfig pet, int level) {
        this.pet = pet;
        this.level = level;
    }

    public void move(Player player) {
        if(itemDisplay == null || slime == null) return;
        if(teleporter == null) {
            teleporter = new SmoothTeleporter(slime, player);
        }
        teleporter.update();

        // Initialize the last player location if it's null
        if (lastPlayerLocation == null) {
            lastPlayerLocation = player.getLocation();
        }

        Location playerLocation = player.getLocation();
        double distance = playerLocation.distance(lastPlayerLocation);

        // Calculate direction vector
        Location petLocation = slime.getLocation();
        double dx = playerLocation.getX() - petLocation.getX();
        double dz = playerLocation.getZ() - petLocation.getZ();


        double idleY = petLocation.getY() + 0.05 * Math.sin(t);
        slime.teleport(new Location(petLocation.getWorld(), petLocation.getX(), idleY, petLocation.getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN, TeleportFlag.EntityState.RETAIN_PASSENGERS);

        lookAtPlayer(player);
        new ParticleBuilder(Particle.REDSTONE).color(pet.color()).location(slime.getLocation()).spawn();
        t += Math.PI / 16;
    }

    private void lookAtPlayer(Player player) {
        if (itemDisplay == null) return;

        Location petLocation = itemDisplay.getLocation();
        Location playerLocation = player.getLocation();

        Vector direction = playerLocation.toVector().subtract(petLocation.toVector());

        float rawYaw = (float) Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));
        float yaw = (rawYaw + 180) % 360;

        itemDisplay.setRotation(yaw, 0);
    }

    public void summon(Player player) {
        if(itemDisplay != null && slime != null) despawn();

        Component component = Component.empty()
                .append(Component.text("[Lvl " + level + "] ").color(TextColor.color(0xAAAAAA)))
                .append(Component.text(pet.displayName()).color(TextColor.color(pet.color().asRGB())));


        itemDisplay = player.getWorld().spawn(player.getLocation(), ItemDisplay.class);
        itemDisplay.setItemStack(pet.getHead());
        itemDisplay.customName(component);
        itemDisplay.setCustomNameVisible(true);

        Transformation transformation = itemDisplay.getTransformation();
        transformation.getScale().set(0.5f,0.5f,0.5f);
        itemDisplay.setTransformation(transformation);

        slime = player.getWorld().spawn(player.getLocation(), Slime.class);
        slime.setAI(false);
        slime.setCollidable(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        slime.setSize(0);

        slime.addPassenger(itemDisplay);

        move(player);
    }

    public void despawn() {
        if(itemDisplay == null || slime == null) return;

        itemDisplay.remove();
        slime.remove();

        slime = null;
        itemDisplay = null;
    }

    public ItemDisplay getArmorStand() {
        return itemDisplay;
    }
}


class SmoothTeleporter {
    private final Entity entity;
    private final Entity target;
    int maxBlocksPerSecond = 11;
    int maxDistance = 50;
    int minDistance = 4;

    public SmoothTeleporter(Entity entity, Entity target) {
        this.entity = entity;
        this.target = target;
    }

    public void update() {
        Location entityLocation = entity.getLocation();
        Location targetLocation = target.getLocation();

        double distance = entityLocation.distance(targetLocation);

        if (distance < minDistance) {
            return;
        }

        if (distance > maxDistance) {
            entity.teleport(targetLocation, PlayerTeleportEvent.TeleportCause.PLUGIN, TeleportFlag.EntityState.RETAIN_PASSENGERS);
            return;
        }

        Vector direction = targetLocation.toVector().subtract(entityLocation.toVector()).normalize();
        Vector velocity = direction.multiply(1.0 / 20 * maxBlocksPerSecond);

        System.out.println("Updating");
        Location newLocation = entityLocation.add(velocity);

        entity.teleport(newLocation, PlayerTeleportEvent.TeleportCause.PLUGIN, TeleportFlag.EntityState.RETAIN_PASSENGERS);
    }
}
