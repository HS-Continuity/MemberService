package com.yeonieum.memberservice.global.converter;

import com.yeonieum.memberservice.global.enums.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Character> {

    @Override
    public Character convertToDatabaseColumn(Gender gender) {
        if (gender == null) {
            return null;
        }
        return gender.getStoredGenderValue();
    }

    @Override
    public Gender convertToEntityAttribute(Character dbData) {
        if (dbData == null) {
            return null;
        }
        return Gender.fromCode(dbData);
    }
}

