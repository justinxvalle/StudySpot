package com.justin.studyspot.service;


import com.justin.studyspot.ScoringService;
import com.justin.studyspot.dto.SpotResponse;
import com.justin.studyspot.model.Report;
import com.justin.studyspot.model.Spot;
import com.justin.studyspot.repository.ReportRepository;
import com.justin.studyspot.repository.SpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class SpotService {

    private final SpotRepository spotRepository;
    private final ReportRepository reportRepository;
    private final ScoringService scoringService;

    public SpotService(
            SpotRepository spotRepository,
            ReportRepository reportRepository,
            ScoringService scoringService
    ) {
        this.spotRepository = spotRepository;
        this.reportRepository = reportRepository;
        this.scoringService = scoringService;
    }

    @Transactional(readOnly = true)
    public List<SpotResponse> getAllSpots() {
        Instant now = Instant.now();

        return spotRepository.findByActiveTrue()
                .stream()
                .map(spot -> {
                    List<Report> reports =
                            reportRepository.findBySpot_SpotIdAndHiddenFalse(spot.getSpotId());

                    return toSpotResponse(spot, reports, now);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<SpotResponse> getSpot(Long spotId) {
        Instant now = Instant.now();

        return spotRepository.findById(spotId)
                .filter(Spot::getActive)
                .map(spot -> {
                    List<Report> reports =
                            reportRepository.findBySpot_SpotIdAndHiddenFalse(spot.getSpotId());

                    return toSpotResponse(spot, reports, now);
                });
    }

    private SpotResponse toSpotResponse(Spot spot, List<Report> reports, Instant now) {
        return new SpotResponse(
                spot.getSpotId(),
                spot.getName(),
                spot.getAddress(),
                spot.getLatitude(),
                spot.getLongitude(),
                spot.getChain(),
                spot.getMapLink(),
                scoringService.scoreOutlets(reports, now).orElse(null),
                scoringService.scoreNoise(reports, now).orElse(null),
                scoringService.scoreWifi(reports, now).orElse(null)
        );
    }
}
