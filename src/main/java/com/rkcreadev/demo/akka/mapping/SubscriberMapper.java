package com.rkcreadev.demo.akka.mapping;

import com.rkcreadev.demo.akka.model.db.cassandra.SubscriberCassandra;
import com.rkcreadev.demo.akka.model.json.SubscriberPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper
public interface SubscriberMapper {
    @Mappings({})
    SubscriberCassandra toCassandraEntity(SubscriberPayment subscriberPayment);
}
