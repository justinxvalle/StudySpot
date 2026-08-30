package com.justin.studyspot.service;

import com.justin.studyspot.dto.ReportRequest;
import com.justin.studyspot.model.Report;
import com.justin.studyspot.model.Spot;
import com.justin.studyspot.repository.ReportRepository;
import com.justin.studyspot.repository.SpotRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;

@Service
public class ReportService {

    private final SpotRepository spotRepository;
    private final ReportRepository reportRepository;
    private final int windowMinutes;
    private final int maxReports;

    public ReportService(
            SpotRepository spotRepository,
            ReportRepository reportRepository,
            @Value("${studyspot.rate-limit.window-minutes}") int windowMinutes,
            @Value("${studyspot.rate-limit.max-reports}") int  maxReports
    ) {
        this.spotRepository = spotRepository;
        this.reportRepository = reportRepository;
        this.windowMinutes = windowMinutes;
        this.maxReports = maxReports;
    }

    @Transactional
    public void createReport(Long spotId, ReportRequest request) {
        Instant since = Instant.now().minus(Duration.ofMinutes(windowMinutes));
        long recent = reportRepository.countByReporterIdAndCreatedAtAfter(request.reporterId(), since);
        if (recent >= maxReports) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Rate limit exceeded, try again later"
            );
        }

        Spot spot = spotRepository.findBySpotIdAndActiveTrue(spotId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Spot not found"
                ));

        Report report = new Report();

        report.setReporterId(request.reporterId());
        report.setSpot(spot);
        report.setOutlets(request.outlets());
        report.setNoise(request.noise());
        report.setHasWifi(request.hasWifi());
        report.setAdditionalComments(request.comments());

        reportRepository.save(report);
    }
}
