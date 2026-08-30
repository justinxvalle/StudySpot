package com.justin.studyspot.repository;

import com.justin.studyspot.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findBySpot_SpotIdAndHiddenFalse(Long spotId);
}
