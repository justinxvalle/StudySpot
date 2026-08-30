package com.justin.studyspot;

public enum NoiseLevel {
    QUIET(0.0), MODERATE(0.5), LOUD(1.0);

    private final double weight;
    NoiseLevel(double weight) { this.weight = weight;}
    public double getWeight() { return this.weight; }
}
