package com.rkcreadev.demo.akka.repository;

import com.rkcreadev.demo.akka.model.db.ClientInfo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientInfoRepository extends CassandraRepository<ClientInfo, Long> {
}
