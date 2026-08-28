package com.justin.studyspot;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScoringServiceTest {
    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");
    ScoringService service = new ScoringService();

    private Report reportWithOutlets(OutletLevel level, double ageDays) {
        Report r = new Report();
        r.setOutlets(level);
        r.setCreatedAt(NOW.minusSeconds((long)(ageDays * 86400)));
        return r;
    }

    private Report reportWithNothing(double ageDays) {
        Report r = new Report();
        r.setCreatedAt(NOW.minusSeconds((long)(ageDays * 86400)));
        return r;
    }

    @Test
    void decayAtOneHalfLifeIsOneHalf() {
        assertEquals(0.5, service.calculateDecay(90), 0.0001);
    }

    @Test
    void singleNoneReportScoresZeroNotEmpty() {
        List<Report> reports = List.of(reportWithOutlets(OutletLevel.NONE, 0));

        Optional<AmenityScore> result = service.scoreOutlets(reports, NOW);

        assertTrue(result.isPresent());
        assertEquals(0.0, result.get().value(), 0.0001);
    }

    @Test
    void manyFreshReportsScoreOne() {
        List<Report> reports = List.of(
                reportWithOutlets(OutletLevel.MANY, 0),
                reportWithOutlets(OutletLevel.MANY, 0),
                reportWithOutlets(OutletLevel.MANY, 0)
        );

        Optional<AmenityScore> result = service.scoreOutlets(reports, NOW);
        assertTrue(result.isPresent());
        assertEquals(1.0, result.get().value(), 0.0001);
    }

    @Test
    void manyTodayPlusNoneAt180DaysScoresAboveZeroPointFive() {
        List<Report> reports = List.of(
                reportWithOutlets(OutletLevel.MANY, 0),
                reportWithOutlets(OutletLevel.NONE, 180)
        );

        Optional<AmenityScore> result = service.scoreOutlets(reports, NOW);
        assertTrue(result.isPresent());
        assertTrue(result.get().value() > 0.5);
    }

    @Test
    void emptyListReturnsEmpty() {
        List<Report> reports = List.of();
        Optional<AmenityScore> result = service.scoreOutlets(reports, NOW);
        assertTrue(result.isEmpty());
    }

    @Test
    void reportWithNullOutletsReturnsEmpty() {
        List<Report> reports = List.of(reportWithNothing(0));
        Optional<AmenityScore> result = service.scoreOutlets(reports, NOW);
        assertTrue(result.isEmpty());
    }

    @Test
    void reportWithOutletsButNoNoiseHasScoreOutletsPresentAndScoreNoiseEmpty() {
        List<Report> reports = List.of(reportWithOutlets(OutletLevel.FEW, 0));
        Optional<AmenityScore> outletResult = service.scoreOutlets(reports, NOW);
        Optional<AmenityScore> noiseResult = service.scoreNoise(reports, NOW);
        assertTrue(outletResult.isPresent());
        assertTrue(noiseResult.isEmpty());
    }

    @Test
    void reportCountCountsOnlyReportsCarryingTheAmenity() {
        List<Report> reports = List.of(
                reportWithOutlets(OutletLevel.NONE, 0),
                reportWithOutlets(OutletLevel.FEW, 0),
                reportWithOutlets(OutletLevel.MANY, 0),
                reportWithNothing(0)
        );

        Optional<AmenityScore> result = service.scoreOutlets(reports, NOW);
        assertTrue(result.isPresent());
        assertEquals(3, result.get().reportCount());
    }

    @Test
    void twoReportsThatDisagreeAtTheSameAgeShouldAverageToZeroPointFive() {
        List<Report> reports = List.of(
                reportWithOutlets(OutletLevel.MANY, 0),
                reportWithOutlets(OutletLevel.NONE, 0)
        );
        Optional<AmenityScore> result = service.scoreOutlets(reports, NOW);
        assertTrue(result.isPresent());
        assertEquals(0.5, result.get().value(), 0.0001);
    }
}
