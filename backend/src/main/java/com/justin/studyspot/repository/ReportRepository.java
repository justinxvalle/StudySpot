package com.justin.studyspot.repository;

import com.justin.studyspot.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findBySpot_SpotIdAndHiddenFalse(Long spotId);
    long countByReporterIdAndCreatedAtAfter(UUID reporterId, Instant since);
}
