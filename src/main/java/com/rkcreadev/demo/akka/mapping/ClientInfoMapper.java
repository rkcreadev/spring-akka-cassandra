package com.rkcreadev.demo.akka.mapping;

import com.rkcreadev.demo.akka.model.db.cassandra.ClientInfoCassandra;
import com.rkcreadev.demo.akka.model.db.cassandra.SubscriberCassandra;
import com.rkcreadev.demo.akka.model.db.common.ClientInfo;
import com.rkcreadev.demo.akka.model.db.jpa.ClientInfoJpa;
import com.rkcreadev.demo.akka.model.db.jpa.SubscriberJpa;
import com.rkcreadev.demo.akka.model.json.ClientInfoDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ClientInfoMapper {

    @Mappings({})
    ClientInfoDto mapToDto(ClientInfo clientInfo);

    @Mappings({
            @Mapping(ignore = true, target = "subscribers")
    })
    ClientInfoJpa toJpaEntity(ClientInfo clientInfo);

    @Mappings({})
    ClientInfoCassandra toCassandraEntity(ClientInfo clientInfo);

    @AfterMapping
    default void setIsBig(ClientInfo clientInfo, @MappingTarget ClientInfoDto dto) {
        dto.setBig(clientInfo.getSubscribers().size() > 100);
    }

    @AfterMapping
    default void setSubscribers(ClientInfo clientInfo, @MappingTarget ClientInfoJpa clientInfoJpa) {
        List<SubscriberJpa> subscribers = clientInfo.getSubscribers()
                .stream()
                .map(subscriberPayment -> {
                    SubscriberJpa subscriberJpa = new SubscriberJpa();
                    subscriberJpa.setSubscriberId(subscriberPayment.getId());
                    subscriberJpa.setSpent(subscriberPayment.getSpent());
                    return subscriberJpa;
                })
                .collect(Collectors.toList());
        clientInfoJpa.setSubscribers(subscribers);
    }
}
