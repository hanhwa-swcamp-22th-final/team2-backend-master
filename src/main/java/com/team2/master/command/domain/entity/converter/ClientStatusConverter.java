package com.team2.master.command.domain.entity.converter;

import com.team2.master.command.domain.entity.enums.ClientStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ClientStatusConverter implements AttributeConverter<ClientStatus, String> {

    @Override
    public String convertToDatabaseColumn(ClientStatus attribute) {
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public ClientStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ClientStatus.fromDbValue(dbData);
    }
}
