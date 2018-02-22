package com.rkcreadev.demo.akka.mapping;

import com.rkcreadev.demo.akka.model.db.ClientInfo;
import com.rkcreadev.demo.akka.model.json.ClientInfoDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper
public interface ClientInfoMapper {

    @Mappings({})
    ClientInfoDto mapToDto(ClientInfo clientInfo);

    @AfterMapping
    default void setIsBig(ClientInfo clientInfo, @MappingTarget ClientInfoDto dto) {
        dto.setBig(clientInfo.getSubscriberIds().size() > 100);
    }
}
