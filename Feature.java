package com.example.targethud;

/** One entry in the GUI: a switch, optionally with a slider. */
public class Feature {
    public final String name;
    public final String panel;
    public boolean on;
    public boolean open;

    public final boolean hasSlider;
    public final double min, max;
    public final boolean integer;
    public final String unit;
    public double value;

    public Feature(String name, String panel, boolean on) {
        this(name, panel, on, false, 0, 0, 0, false, "");
    }

    public Feature(String name, String panel, boolean on, double min, double max, double value, boolean integer, String unit) {
        this(name, panel, on, true, min, max, value, integer, unit);
    }

    private Feature(String name, String panel, boolean on, boolean hasSlider, double min, double max,
                    double value, boolean integer, String unit) {
        this.name = name;
        this.panel = panel;
        this.on = on;
        this.hasSlider = hasSlider;
        this.min = min;
        this.max = max;
        this.value = value;
        this.integer = integer;
        this.unit = unit;
    }

    public String key() {
        return name.toLowerCase().replace(' ', '_');
    }
}
