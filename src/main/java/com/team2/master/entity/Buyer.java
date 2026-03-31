package com.team2.master.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "buyers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buyer_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "buyer_name", nullable = false, length = 100)
    private String buyerName;

    @Column(name = "buyer_position", length = 100)
    private String buyerPosition;

    @Column(name = "buyer_email", length = 255)
    private String buyerEmail;

    @Column(name = "buyer_tel", length = 50)
    private String buyerTel;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Buyer(Client client, String buyerName, String buyerPosition,
                 String buyerEmail, String buyerTel) {
        if (client == null) {
            throw new IllegalArgumentException("거래처는 필수입니다.");
        }
        if (buyerName == null) {
            throw new IllegalArgumentException("바이어명은 필수입니다.");
        }
        this.client = client;
        this.buyerName = buyerName;
        this.buyerPosition = buyerPosition;
        this.buyerEmail = buyerEmail;
        this.buyerTel = buyerTel;
    }

    public void updateInfo(String buyerName, String buyerPosition,
                           String buyerEmail, String buyerTel) {
        if (buyerName != null) this.buyerName = buyerName;
        if (buyerPosition != null) this.buyerPosition = buyerPosition;
        if (buyerEmail != null) this.buyerEmail = buyerEmail;
        if (buyerTel != null) this.buyerTel = buyerTel;
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
