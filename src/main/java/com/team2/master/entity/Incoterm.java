package com.team2.master.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "incoterms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Incoterm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incoterm_id")
    private Integer id;

    @Column(name = "incoterm_code", nullable = false, unique = true, length = 10)
    private String incotermCode;

    @Column(name = "incoterm_name", nullable = false, length = 200)
    private String incotermName;

    @Column(name = "incoterm_name_kr", length = 200)
    private String incotermNameKr;

    @Column(name = "incoterm_description", columnDefinition = "TEXT")
    private String incotermDescription;

    @Column(name = "incoterm_transport_mode", length = 50)
    private String incotermTransportMode;

    @Column(name = "incoterm_seller_segments", length = 50)
    private String incotermSellerSegments;

    @Column(name = "incoterm_default_named_place", length = 100)
    private String incotermDefaultNamedPlace;

    public Incoterm(String incotermCode, String incotermName, String incotermNameKr,
                    String incotermDescription, String incotermTransportMode,
                    String incotermSellerSegments, String incotermDefaultNamedPlace) {
        if (incotermCode == null) {
            throw new IllegalArgumentException("인코텀 코드는 필수입니다.");
        }
        if (incotermName == null) {
            throw new IllegalArgumentException("인코텀명은 필수입니다.");
        }
        this.incotermCode = incotermCode;
        this.incotermName = incotermName;
        this.incotermNameKr = incotermNameKr;
        this.incotermDescription = incotermDescription;
        this.incotermTransportMode = incotermTransportMode;
        this.incotermSellerSegments = incotermSellerSegments;
        this.incotermDefaultNamedPlace = incotermDefaultNamedPlace;
    }

    public void update(String incotermCode, String incotermName, String incotermNameKr,
                       String incotermDescription, String incotermTransportMode,
                       String incotermSellerSegments, String incotermDefaultNamedPlace) {
        this.incotermCode = incotermCode;
        this.incotermName = incotermName;
        this.incotermNameKr = incotermNameKr;
        this.incotermDescription = incotermDescription;
        this.incotermTransportMode = incotermTransportMode;
        this.incotermSellerSegments = incotermSellerSegments;
        this.incotermDefaultNamedPlace = incotermDefaultNamedPlace;
    }
}
