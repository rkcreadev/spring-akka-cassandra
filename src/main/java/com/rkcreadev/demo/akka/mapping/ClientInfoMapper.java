package com.rkcreadev.demo.akka.mapping;

import com.rkcreadev.demo.akka.model.db.cassandra.ClientInfoCassandra;
import com.rkcreadev.demo.akka.model.db.common.ClientInfo;
import com.rkcreadev.demo.akka.model.db.jpa.ClientInfoJpa;
import com.rkcreadev.demo.akka.model.json.ClientInfoDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper
public interface ClientInfoMapper {

    @Mappings({})
    ClientInfoDto mapToDto(ClientInfo clientInfo);

    @Mappings({})
    ClientInfoJpa toJpaEntity(ClientInfo clientInfo);

    @Mappings({})
    ClientInfoCassandra toCassandraEntity(ClientInfo clientInfo);

    @AfterMapping
    default void setIsBig(ClientInfo clientInfo, @MappingTarget ClientInfoDto dto) {
        dto.setBig(clientInfo.getSubscribers().size() > 100);
    }
}
