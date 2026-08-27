package com.justin.studyspot;

/**
 *
 * @param value - the weighted average
 * @param confidence - the sum of decay weights
 * @param reportCount - for display
 */
public record AmenityScore(double value, double confidence, int reportCount) {

}
