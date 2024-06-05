package de.bybackfish.clickerfymuseum.config;


import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public record PetConfig(

    String name,
    String displayName,
    String texture,
    Color color
) {


    public ItemStack getHead() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", texture()));

        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        itemMeta.setPlayerProfile(profile);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
