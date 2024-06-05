package de.bybackfish.clickerfypet;

import de.bybackfish.clickerfypet.commands.SetPetCommand;
import de.bybackfish.clickerfypet.config.PetConfigLoader;
import de.bybackfish.clickerfypet.listener.PlayerListener;
import de.bybackfish.clickerfypet.pet.PlayerPet;
import de.bybackfish.clickerfypet.pet.PlayerPetManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClickerifyPet extends JavaPlugin {

    private PlayerPetManager playerPetManager;
    private PetConfigLoader petConfigLoader;

    @Override
    public void onEnable() {
        petConfigLoader = new PetConfigLoader();
        playerPetManager = new PlayerPetManager(petConfigLoader);

        getServer().getPluginManager().registerEvents(new PlayerListener(playerPetManager), this);

        SetPetCommand setPetCommand = new SetPetCommand(playerPetManager);
        getCommand("setdisplaypet").setExecutor(setPetCommand);
        getCommand("setdisplaypet").setTabCompleter(setPetCommand);

        saveDefaultConfig();
        petConfigLoader.loadPets(getConfig());

        Runnable movePets =  () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                PlayerPet playerPet = playerPetManager.getPet(player);
                if(playerPet != null)
                    playerPet.move(player);
            }
        };

        Bukkit.getScheduler().runTaskTimer(this, movePets, 0, 1);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerPet playerPet = playerPetManager.getPet(player);
            if(playerPet != null)
                playerPet.despawn();
        }
    }
}
