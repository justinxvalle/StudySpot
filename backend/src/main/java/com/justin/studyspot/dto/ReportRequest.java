package com.justin.studyspot;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ReportRequest(
        OutletLevel outlets,
        NoiseLevel noise,
        Boolean hasWifi,
        @NotNull UUID reporterId,
        @Size(max = 500) String comments
) {}
