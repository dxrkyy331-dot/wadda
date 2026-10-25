package com.example.targethud;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

/** All features of the GUI and their saved values. */
public final class TargetHudConfig {
    public static final String[] PANELS = {"HUD", "Effects"};

    public static final Feature TARGET = new Feature("Target HUD", "HUD", true);
    public static final Feature COORDS = new Feature("Coordinates", "HUD", false);
    public static final Feature FACING = new Feature("Facing", "HUD", false);

    public static final Feature FOG = new Feature("Fog", "Effects", false, 0, 100, 30, true, "%");
    public static final Feature CRYSTALS = new Feature("Crystals", "Effects", false, 0, 30, 9, true, "");

    public static final List<Feature> ALL = List.of(TARGET, COORDS, FACING, FOG, CRYSTALS);

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("targethud.properties");
    }

    public static void load() {
        Path f = file();
        if (!Files.exists(f)) return;
        try (Reader r = Files.newBufferedReader(f)) {
            Properties p = new Properties();
            p.load(r);
            for (Feature m : ALL) {
                m.on = Boolean.parseBoolean(p.getProperty("on." + m.key(), String.valueOf(m.on)));
                if (m.hasSlider) {
                    double v = Double.parseDouble(p.getProperty("value." + m.key(), String.valueOf(m.value)));
                    m.value = Math.max(m.min, Math.min(m.max, v));
                }
            }
        } catch (IOException | NumberFormatException ignored) {
            // keep defaults when the file is missing or broken
        }
    }

    public static void save() {
        Properties p = new Properties();
        for (Feature m : ALL) {
            p.setProperty("on." + m.key(), String.valueOf(m.on));
            if (m.hasSlider) p.setProperty("value." + m.key(), String.valueOf(m.value));
        }
        try (Writer w = Files.newBufferedWriter(file())) {
            p.store(w, "Target HUD settings");
        } catch (IOException ignored) {
            // nothing to do if the config folder is not writable
        }
    }

    public static List<Feature> in(String panel) {
        return ALL.stream().filter(m -> m.panel.equals(panel)).toList();
    }
}
