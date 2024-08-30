package io.github.bilektugrul.wgblockflags;

import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RegionDataContainer {

    private final WGBlockFlags plugin;

    private final Map<String, List<String>> placeableBlocks = new HashMap<>();
    private final Map<String, List<String>> breakableBlocks = new HashMap<>();

    public RegionDataContainer(WGBlockFlags plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        placeableBlocks.clear();
        breakableBlocks.clear();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("regions");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            List<String> placeables = section.getStringList(key + ".placeable-blocks");
            List<String> breakables = section.getStringList(key + ".breakable-blocks");

            for (String placeable : placeables) {
                addPlaceable(key, placeable);
            }

            for (String breakable : breakables) {
                addBreakable(key, breakable);
            }
        }
    }

    public void save() {
        for (String region : placeableBlocks.keySet()) {
            List<String> placeables = placeableBlocks.get(region);

            plugin.getConfig().set("regions." + region + ".placeable-blocks", placeables);
        }

        for (String region : breakableBlocks.keySet()) {
            List<String> breakables = breakableBlocks.get(region);

            plugin.getConfig().set("regions." + region + ".breakable-blocks", breakables);
        }

        try {
            plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPlaceable(String region, String material) {
        if (!placeableBlocks.containsKey(region)) {
            placeableBlocks.put(region, new ArrayList<>());
        }

        List<String> placeables = getPlaceableBlocksOf(region);
        placeables.add(material.toUpperCase(Locale.ROOT));

        placeableBlocks.put(region, placeables);
    }

    public void addBreakable(String region, String material) {
        if (!breakableBlocks.containsKey(region)) {
            breakableBlocks.put(region, new ArrayList<>());
        }

        List<String> breakables = getBreakableBlocksOf(region);
        breakables.add(material.toUpperCase(Locale.ROOT));

        breakableBlocks.put(region, breakables);
    }

    public void removeBreakable(String region, String material) {
        if (!breakableBlocks.containsKey(region)) {
            return;
        }

        List<String> breakables = getBreakableBlocksOf(region);
        breakables.removeIf(s -> s.equalsIgnoreCase(material));

        breakableBlocks.put(region, breakables);
    }

    public void removePlaceable(String region, String material) {
        if (!placeableBlocks.containsKey(region)) {
            return;
        }

        List<String> placeables = getPlaceableBlocksOf(region);
        placeables.removeIf(s -> s.equalsIgnoreCase(material));

        placeableBlocks.put(region, placeables);
    }

    public List<String> getPlaceableBlocksOf(String region) {
        return placeableBlocks.get(region);
    }

    public List<String> getBreakableBlocksOf(String region) {
        return breakableBlocks.get(region);
    }

}
