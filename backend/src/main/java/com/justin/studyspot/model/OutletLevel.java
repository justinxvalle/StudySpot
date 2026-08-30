package com.justin.studyspot;

public enum OutletLevel {
    NONE(0.0), FEW(0.5), MANY(1.0);

    private final double weight;
    OutletLevel(double weight) { this.weight = weight; }
    public double getWeight() { return this.weight; }
}
