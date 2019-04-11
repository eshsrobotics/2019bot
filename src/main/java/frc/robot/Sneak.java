package frc.robot;

public class Sneak {

    private double currentSneakValue;
    private double activeSneakValue;

    public Sneak(double sneakValue) {
        this.currentSneakValue = 1.0;
        this.activeSneakValue = sneakValue;
    }
    public double get() { return this.currentSneakValue; }
    public void enable() { currentSneakValue = activeSneakValue; }
    public void disable() { currentSneakValue = 1.0; }
    public boolean enabled() { return (currentSneakValue == activeSneakValue); }
}