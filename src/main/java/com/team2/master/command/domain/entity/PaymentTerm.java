package com.team2.master.command.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_terms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_term_id")
    private Integer paymentTermId;

    @Column(name = "payment_term_code", nullable = false, unique = true, length = 20)
    private String paymentTermCode;

    @Column(name = "payment_term_name", nullable = false, length = 100)
    private String paymentTermName;

    @Column(name = "payment_term_description", length = 200)
    private String paymentTermDescription;

    public PaymentTerm(String paymentTermCode, String paymentTermName, String paymentTermDescription) {
        if (paymentTermCode == null) {
            throw new IllegalArgumentException("결제조건 코드는 필수입니다.");
        }
        if (paymentTermName == null) {
            throw new IllegalArgumentException("결제조건명은 필수입니다.");
        }
        this.paymentTermCode = paymentTermCode;
        this.paymentTermName = paymentTermName;
        this.paymentTermDescription = paymentTermDescription;
    }

    public void update(String paymentTermCode, String paymentTermName, String paymentTermDescription) {
        this.paymentTermCode = paymentTermCode;
        this.paymentTermName = paymentTermName;
        this.paymentTermDescription = paymentTermDescription;
    }
}
