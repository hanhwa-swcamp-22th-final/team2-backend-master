package com.team2.master.command.domain.entity;

import com.team2.master.command.domain.entity.converter.ClientStatusConverter;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "client_code", nullable = false, unique = true, length = 20)
    private String clientCode;

    @Column(name = "client_name", nullable = false, length = 100)
    private String clientName;

    @Column(name = "client_name_kr", length = 100)
    private String clientNameKr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "client_city", length = 100)
    private String clientCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "port_id")
    private Port port;

    @Column(name = "client_address", columnDefinition = "TEXT")
    private String clientAddress;

    @Column(name = "client_tel", length = 50)
    private String clientTel;

    @Column(name = "client_email", length = 255)
    private String clientEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_term_id")
    private PaymentTerm paymentTerm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "client_manager", length = 100)
    private String clientManager;

    @Column(name = "team_id")
    private Integer teamId;

    @Convert(converter = ClientStatusConverter.class)
    @Column(name = "client_status", nullable = false)
    private ClientStatus clientStatus;

    @Column(name = "client_reg_date")
    private LocalDate clientRegDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Client(String clientCode, String clientName, String clientNameKr,
                  String clientCity, String clientAddress, String clientTel,
                  String clientEmail, String clientManager, Integer teamId,
                  ClientStatus clientStatus, LocalDate clientRegDate) {
        this.clientCode = clientCode;
        this.clientName = clientName;
        this.clientNameKr = clientNameKr;
        this.clientCity = clientCity;
        this.clientAddress = clientAddress;
        this.clientTel = clientTel;
        this.clientEmail = clientEmail;
        this.clientManager = clientManager;
        this.teamId = teamId;
        this.clientStatus = clientStatus != null ? clientStatus : ClientStatus.ACTIVE;
        this.clientRegDate = clientRegDate;
    }

    public void changeStatus(ClientStatus newStatus) {
        if (this.clientStatus == newStatus) {
            throw new IllegalStateException("이미 " + newStatus + " 상태입니다.");
        }
        this.clientStatus = newStatus;
    }

    public void updateInfo(String clientName, String clientNameKr, String clientCity,
                           String clientAddress, String clientTel, String clientEmail,
                           String clientManager, Integer teamId) {
        if (clientName != null) this.clientName = clientName;
        if (clientNameKr != null) this.clientNameKr = clientNameKr;
        if (clientCity != null) this.clientCity = clientCity;
        if (clientAddress != null) this.clientAddress = clientAddress;
        if (clientTel != null) this.clientTel = clientTel;
        if (clientEmail != null) this.clientEmail = clientEmail;
        if (clientManager != null) this.clientManager = clientManager;
        if (teamId != null) this.teamId = teamId;
    }

    public void assignCountry(Country country) {
        this.country = country;
    }

    public void assignPort(Port port) {
        this.port = port;
    }

    public void assignPaymentTerm(PaymentTerm paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public void assignCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isActive() {
        return this.clientStatus == ClientStatus.ACTIVE;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
