package com.rkcreadev.demo.akka.model.db.cassandra;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Table("benchmark")
@Data
public class BenchmarkCassandra {
    @Id
    private UUID id = UUID.randomUUID();
    @Column
    private String runId;
    @Column
    private Long filesCount;
    @Column
    private Long milliseconds;
    @Column
    private Date startDate;
}
