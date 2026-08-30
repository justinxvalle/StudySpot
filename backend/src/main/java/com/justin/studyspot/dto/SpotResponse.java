package com.justin.studyspot;

import java.math.BigDecimal;

public record SpotResponse(
        Long spotId,
        String spotName,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String chain,
        String mapLink,
        AmenityScore outlets,
        AmenityScore noise,
        AmenityScore wifi
) {
}
