package com.justin.studyspot;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
public class SpotController {

    private final SpotService spotService;
    private final ReportService reportService;

    public SpotController(SpotService spotService, ReportService reportService) {
        this.spotService = spotService;
        this.reportService = reportService;
    }

    @GetMapping
    public List<SpotResponse> getSpots() {
        return spotService.getAllSpots();
    }

    @GetMapping("/{spotId}")
    public ResponseEntity<SpotResponse> getSpot(@PathVariable Long spotId) {
        return spotService.getSpot(spotId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{spotId}/reports")
    public ResponseEntity<Void> createReport(
            @PathVariable Long spotId,
            @Valid @RequestBody ReportRequest request
    ) {
       reportService.createReport(spotId, request);
       return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
