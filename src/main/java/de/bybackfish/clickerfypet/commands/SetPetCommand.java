package de.bybackfish.clickerfymuseum.commands;

import de.bybackfish.clickerfymuseum.config.PetConfig;
import de.bybackfish.clickerfymuseum.config.PetConfigLoader;
import de.bybackfish.clickerfymuseum.pet.PlayerPet;
import de.bybackfish.clickerfymuseum.pet.PlayerPetManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SetPetCommand implements CommandExecutor, TabCompleter {

    List<String> aHundredNumbers = IntStream.rangeClosed(1, 100).boxed().map(Objects::toString).collect(Collectors.toList());

    private final PlayerPetManager playerPetManager;

    public SetPetCommand(PlayerPetManager playerPetManager) {
        this.playerPetManager = playerPetManager;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!commandSender.hasPermission("clickerfy.petdisplay.set")) return false;

        if(strings.length < 3) return false;

        String playerName = strings[0];
        String petName = strings[1];
        int level = Integer.parseInt(strings[2]);

        Player player = Bukkit.getPlayer(playerName);
        if(player == null) {
            commandSender.sendMessage("Player not found");
            return false;
        }

        PetConfig petConfig = playerPetManager.getConfigLoader().getPet(petName);
        if(petConfig == null) {
            commandSender.sendMessage("Pet not found");
            return false;
        }

        playerPetManager.setPet(player, new PlayerPet(petConfig, level));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return ((switch (strings.length) {
            case 1 -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            case 2 -> playerPetManager.getConfigLoader().getPets().stream().map(PetConfig::name).toList();
            case 3 -> aHundredNumbers;
            default -> List.of("");
        }).stream().filter(e -> e.startsWith(strings[strings.length - 1])).toList());

    }
}
