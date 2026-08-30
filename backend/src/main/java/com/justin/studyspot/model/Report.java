package com.justin.studyspot;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "reports")
public class Report {

    @Id
    @Column(name = "report_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(name = "reporter_id", nullable = false)
    private UUID reporterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @Column(name = "outlets")
    @Enumerated(EnumType.STRING)
    private OutletLevel outlets;

    @Column(name = "noise")
    @Enumerated(EnumType.STRING)
    private NoiseLevel noise;

    @Column(name = "has_wifi")
    private Boolean hasWifi;

    @Column(name = "hidden", nullable = false)
    private Boolean hidden = false;

    @Column(name = "additional_comments")
    private String additionalComments;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    public Report() {}

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public UUID getReporterId() {
        return reporterId;
    }

    public void setReporterId(UUID reporterId) {
        this.reporterId = reporterId;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public OutletLevel getOutlets() {
        return outlets;
    }

    public void setOutlets(OutletLevel outlets) {
        this.outlets = outlets;
    }

    public NoiseLevel getNoise() {
        return noise;
    }

    public void setNoise(NoiseLevel noise) {
        this.noise = noise;
    }

    public Boolean getHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(Boolean hasWifi) {
        this.hasWifi = hasWifi;
    }

    public Boolean getHidden() {
        return this.hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", reporterId=" + reporterId +
                ", spotId=" + (spot != null ? spot.getSpotId() : null) +
                ", outlets='" + outlets + '\'' +
                ", noise='" + noise + '\'' +
                ", hasWifi=" + hasWifi +
                ", hidden=" + hidden +
                ", additionalComments='" + additionalComments + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
