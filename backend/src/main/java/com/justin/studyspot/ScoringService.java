package com.justin.studyspot;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 This class takes an amenity (outlets, noise, hasWifi) and calculates its AmenityScore.

 Each amenity is scored as a decay-weighted average across a spot's reports using the
 following formula:
    weight * exp(-ln(2) * age / halfLife), divided by the sum of decay factors

 A 90-day half life was chosen because outlet availability changes when a cafe renovates
 or rearranges furniture, which can happen on a scale of months.
 */
@Service
public class ScoringService {
    private static final int HALF_LIFE_DAYS = 90;

    // Formula used to calculate
    public double calculateDecay(double ageDays) {
        return Math.exp(-Math.log(2) * ageDays / HALF_LIFE_DAYS );
    }

    // The actual scoring function
    public Optional<AmenityScore> score(List<Report> reports,
                                        Function<Report, Double> weightExtractor,
                                        Instant now) {
        double weightedSum = 0.0;
        double decaySum = 0.0;
        int count = 0;

        // Cycle through each report, extract weights, accumulate results
        for (Report report : reports) {
            Double weight = weightExtractor.apply(report);

            // If the report does not contain useful information -> skip
            if (weight == null)
                continue;

            double ageDays = Duration.between(report.getCreatedAt(), now).toSeconds() / 86400.0;
            double decay = calculateDecay(ageDays);
            weightedSum += weight * decay;
            decaySum += decay;
            count++;
        }

        // In the event that all reports contained no useful information -> return nothing
        if (decaySum == 0.0) {
            return Optional.empty();
        }

        return Optional.of(new AmenityScore(weightedSum / decaySum, decaySum, count));
    }

    public Optional<AmenityScore> scoreOutlets(List<Report> reports, Instant now) {
        return score(reports,
                r -> r.getOutlets() == null ? null : r.getOutlets().getWeight(),
                now);
    }

    public Optional<AmenityScore> scoreNoise(List<Report> reports, Instant now) {
        return score(reports,
                r -> r.getNoise() == null ? null : r.getNoise().getWeight(),
                now);
    }

    public Optional<AmenityScore> scoreWifi(List<Report> reports, Instant now) {
        return score(reports,
                r -> r.getHasWifi() == null ? null : (r.getHasWifi() ? 1.0 : 0.0),
                now);
    }
}
