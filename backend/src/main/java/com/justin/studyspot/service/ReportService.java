package com.justin.studyspot.service;

import com.justin.studyspot.dto.ReportRequest;
import com.justin.studyspot.model.Report;
import com.justin.studyspot.model.Spot;
import com.justin.studyspot.repository.ReportRepository;
import com.justin.studyspot.repository.SpotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReportService {

    private final SpotRepository spotRepository;
    private final ReportRepository reportRepository;

    public ReportService(
            SpotRepository spotRepository,
            ReportRepository reportRepository
    ) {
        this.spotRepository = spotRepository;
        this.reportRepository = reportRepository;
    }

    @Transactional
    public void createReport(Long spotId, ReportRequest request) {
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
