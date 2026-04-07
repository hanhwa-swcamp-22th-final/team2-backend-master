package com.team2.master.command.domain.entity;

import com.team2.master.command.domain.entity.converter.ItemStatusConverter;
import com.team2.master.command.domain.entity.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "item_code", nullable = false, unique = true, length = 50)
    private String itemCode;

    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    @Column(name = "item_name_kr", length = 100)
    private String itemNameKr;

    @Column(name = "item_spec", length = 200)
    private String itemSpec;

    @Column(name = "item_unit", length = 50)
    private String itemUnit;

    @Column(name = "item_pack_unit", length = 50)
    private String itemPackUnit;

    @Column(name = "item_unit_price", precision = 15, scale = 2)
    private BigDecimal itemUnitPrice;

    @Column(name = "item_weight", precision = 10, scale = 3)
    private BigDecimal itemWeight;

    @Column(name = "item_hs_code", length = 20)
    private String itemHsCode;

    @Column(name = "item_category", length = 100)
    private String itemCategory;

    @Convert(converter = ItemStatusConverter.class)
    @Column(name = "item_status", nullable = false)
    private ItemStatus itemStatus;

    @Column(name = "item_reg_date")
    private LocalDate itemRegDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Item(String itemCode, String itemName, String itemNameKr, String itemSpec,
                String itemUnit, String itemPackUnit, BigDecimal itemUnitPrice,
                BigDecimal itemWeight, String itemHsCode, String itemCategory,
                ItemStatus itemStatus, LocalDate itemRegDate) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemNameKr = itemNameKr;
        this.itemSpec = itemSpec;
        this.itemUnit = itemUnit;
        this.itemPackUnit = itemPackUnit;
        this.itemUnitPrice = itemUnitPrice;
        this.itemWeight = itemWeight;
        this.itemHsCode = itemHsCode;
        this.itemCategory = itemCategory;
        this.itemStatus = itemStatus != null ? itemStatus : ItemStatus.ACTIVE;
        this.itemRegDate = itemRegDate;
    }

    public void changeStatus(ItemStatus newStatus) {
        if (this.itemStatus == newStatus) {
            throw new IllegalStateException("이미 " + newStatus + " 상태입니다.");
        }
        this.itemStatus = newStatus;
    }

    public void updateInfo(String itemName, String itemNameKr, String itemSpec,
                           String itemUnit, String itemPackUnit, BigDecimal itemUnitPrice,
                           BigDecimal itemWeight, String itemHsCode, String itemCategory) {
        if (itemName != null) this.itemName = itemName;
        if (itemNameKr != null) this.itemNameKr = itemNameKr;
        if (itemSpec != null) this.itemSpec = itemSpec;
        if (itemUnit != null) this.itemUnit = itemUnit;
        if (itemPackUnit != null) this.itemPackUnit = itemPackUnit;
        if (itemUnitPrice != null) this.itemUnitPrice = itemUnitPrice;
        if (itemWeight != null) this.itemWeight = itemWeight;
        if (itemHsCode != null) this.itemHsCode = itemHsCode;
        if (itemCategory != null) this.itemCategory = itemCategory;
    }

    public Integer getId() { return itemId; }

    public boolean isActive() {
        return this.itemStatus == ItemStatus.ACTIVE;
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
