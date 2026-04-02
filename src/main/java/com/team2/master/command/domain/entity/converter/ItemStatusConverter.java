package com.team2.master.command.domain.entity.converter;

import com.team2.master.command.domain.entity.enums.ItemStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ItemStatusConverter implements AttributeConverter<ItemStatus, String> {

    @Override
    public String convertToDatabaseColumn(ItemStatus attribute) {
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public ItemStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ItemStatus.fromDbValue(dbData);
    }
}
