package com.justin.studyspot.repository;

import com.justin.studyspot.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findByActiveTrue();

    Optional<Spot> findBySpotIdAndActiveTrue(Long spotId);
}
