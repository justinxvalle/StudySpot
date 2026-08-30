package com.justin.studyspot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findByActiveTrue();

    Optional<Spot> findBySpotIdAndActiveTrue(Long spotId);
}
