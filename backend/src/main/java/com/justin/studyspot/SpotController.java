package com.justin.studyspot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
public class SpotController {

    private final SpotService spotService;

    public SpotController(SpotService spotService) {
        this.spotService = spotService;
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
}
