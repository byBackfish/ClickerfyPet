package de.bybackfish.clickerfymuseum.config;

import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetConfigLoader {

    private final Map<String, PetConfig> petConfigs = new HashMap<>();

    public void loadPets(FileConfiguration configuration) {
        configuration.getList("pets").forEach(pet -> {
           if (pet instanceof Map) {
                Map<String, Object> petMap = (Map<String, Object>) pet;
                String petName = (String) petMap.get("name");
                String displayName = (String) petMap.get("displayName");
                String texture = (String) petMap.get("texture");
                Color petColor = Color.fromRGB((int) petMap.get("color"));
                petConfigs.put(petName, new PetConfig(petName, displayName, texture, petColor));
            }
        });
    }

    public PetConfig getPet(String petName) {
        return petConfigs.get(petName);
    }

    public Collection<PetConfig> getPets() {
        return petConfigs.values();
    }

}
