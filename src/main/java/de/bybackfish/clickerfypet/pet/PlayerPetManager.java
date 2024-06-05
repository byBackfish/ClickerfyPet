package de.bybackfish.clickerfymuseum.pet;

import de.bybackfish.clickerfymuseum.config.PetConfig;
import de.bybackfish.clickerfymuseum.config.PetConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerPetManager {

    private final PetConfigLoader petConfigLoader;
    private final Map<UUID, PlayerPet> playerPets;

    public PlayerPetManager(PetConfigLoader petConfigLoader) {
        this.petConfigLoader = petConfigLoader;
        this.playerPets = new HashMap<>();
    }

    public void setPet(Player player, PlayerPet pet) {
        if(getPet(player.getUniqueId()) != null) {
            getPet(player.getUniqueId()).despawn();
        }

        if(pet == null) {
            playerPets.remove(player.getUniqueId());
            return;
        }
        playerPets.put(player.getUniqueId(), pet);

        pet.summon(player);
    }

    public PlayerPet getPet(UUID uuid) {
        return playerPets.get(uuid);
    }

    public PlayerPet getPet(Player player) {
        return getPet(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        playerPets.remove(player.getUniqueId());
    }

    public PetConfigLoader getConfigLoader() {
        return petConfigLoader;
    }
}
