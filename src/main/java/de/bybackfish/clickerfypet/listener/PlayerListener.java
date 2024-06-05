package de.bybackfish.clickerfymuseum.listener;

import de.bybackfish.clickerfymuseum.pet.PlayerPetManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final PlayerPetManager playerPetManager;

    public PlayerListener(PlayerPetManager playerPetManager) {
        this.playerPetManager = playerPetManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerPetManager.removePlayer(event.getPlayer());
    }
}
