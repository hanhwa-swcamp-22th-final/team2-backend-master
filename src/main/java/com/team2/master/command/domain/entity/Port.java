package com.team2.master.command.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Port {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "port_id")
    private Integer portId;

    @Column(name = "port_code", nullable = false, unique = true, length = 20)
    private String portCode;

    @Column(name = "port_name", nullable = false, length = 100)
    private String portName;

    @Column(name = "port_city", length = 100)
    private String portCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    public Port(String portCode, String portName, String portCity, Country country) {
        if (portCode == null) {
            throw new IllegalArgumentException("항구 코드는 필수입니다.");
        }
        if (portName == null) {
            throw new IllegalArgumentException("항구명은 필수입니다.");
        }
        if (country == null) {
            throw new IllegalArgumentException("국가는 필수입니다.");
        }
        this.portCode = portCode;
        this.portName = portName;
        this.portCity = portCity;
        this.country = country;
    }

    public void update(String portCode, String portName, String portCity, Country country) {
        this.portCode = portCode;
        this.portName = portName;
        this.portCity = portCity;
        this.country = country;
    }
}
