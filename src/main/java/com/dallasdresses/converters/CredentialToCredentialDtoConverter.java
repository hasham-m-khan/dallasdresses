package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.CredentialDto;
import com.dallasdresses.entities.Credential;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CredentialToCredentialDtoConverter implements Converter<Credential, CredentialDto> {

    @Override
    public CredentialDto convert(Credential source) {
        return CredentialDto.builder()
                .id(source.getId())
                .userId(source.getUser().getId())
                .providerId(source.getProviderId())
                .providerKey(source.getProviderKey())
                .build();
    }
}
