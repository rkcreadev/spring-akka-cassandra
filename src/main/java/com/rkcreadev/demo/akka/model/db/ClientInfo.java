package com.rkcreadev.demo.akka.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;

@Table("client_info")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientInfo {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private Long clientId;
    @Column
    private Long spentTotal;
    @Column
    private Set<Long> subscriberIds;
}
