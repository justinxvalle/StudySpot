package com.justin.studyspot;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "spots")
public class Spot {

    @Id
    @Column(name = "spot_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false)
    private BigDecimal longitude;

    @Column(name = "chain", nullable = true)
    private String chain;

    @Column(name = "map_link", nullable = false)
    private String mapLink;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    public Spot() {}

    public Long getSpotId() {
        return this.spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getChain() {
        return this.chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public String getMapLink() {
        return this.mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "spotId=" + spotId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", chain='" + chain + '\'' +
                ", map_link='" + mapLink + '\'' +
                ", active=" + active +
                '}';
    }
}
