package com.rkcreadev.demo.akka.model.db.cassandra;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;


@Table("subscriber")
@Data
public class SubscriberCassandra {
    @Id
    private Long id;
    @Column
    private Long spent;
}
