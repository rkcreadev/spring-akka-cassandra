package com.rkcreadev.demo.akka.repository.cassandra;

import com.rkcreadev.demo.akka.model.db.cassandra.BenchmarkCassandra;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CassandraBenchmarkRepository extends CassandraRepository<BenchmarkCassandra, Long> {
}
