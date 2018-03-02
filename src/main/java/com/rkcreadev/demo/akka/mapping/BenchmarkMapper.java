package com.rkcreadev.demo.akka.mapping;

import com.rkcreadev.demo.akka.model.db.cassandra.BenchmarkCassandra;
import com.rkcreadev.demo.akka.model.db.common.Benchmark;
import com.rkcreadev.demo.akka.model.db.jpa.JpaBenchmark;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper
public interface BenchmarkMapper {

    @Mappings({})
    public JpaBenchmark toJpaEntity(Benchmark benchmark);

    @Mappings({})
    public BenchmarkCassandra toCassandraEntity(Benchmark benchmark);
}
