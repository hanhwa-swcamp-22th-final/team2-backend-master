package com.team2.master.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "currencies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private Integer id;

    @Column(name = "currency_code", nullable = false, unique = true, length = 10)
    private String currencyCode;

    @Column(name = "currency_name", nullable = false, length = 100)
    private String currencyName;

    @Column(name = "currency_symbol", length = 5)
    private String currencySymbol;

    public Currency(String currencyCode, String currencyName, String currencySymbol) {
        if (currencyCode == null) {
            throw new IllegalArgumentException("통화 코드는 필수입니다.");
        }
        if (currencyName == null) {
            throw new IllegalArgumentException("통화명은 필수입니다.");
        }
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
    }

    public void update(String currencyCode, String currencyName, String currencySymbol) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
    }
}
