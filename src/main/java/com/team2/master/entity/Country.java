package com.team2.master.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "countries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Integer id;

    @Column(name = "country_code", nullable = false, unique = true, length = 10)
    private String countryCode;

    @Column(name = "country_name", nullable = false, length = 100)
    private String countryName;

    @Column(name = "country_name_kr", length = 100)
    private String countryNameKr;

    public Country(String countryCode, String countryName, String countryNameKr) {
        if (countryCode == null) {
            throw new IllegalArgumentException("국가 코드는 필수입니다.");
        }
        if (countryName == null) {
            throw new IllegalArgumentException("국가명은 필수입니다.");
        }
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.countryNameKr = countryNameKr;
    }

    public void update(String countryCode, String countryName, String countryNameKr) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.countryNameKr = countryNameKr;
    }
}
