package com.rkcreadev.demo.akka.service;

import com.rkcreadev.demo.akka.model.db.common.Benchmark;
import com.rkcreadev.demo.akka.model.db.common.ClientInfo;

public interface DbVisitor {

    void jpaSave(ClientInfo clientInfo);

    void cassandraSave(ClientInfo clientInfo);

    boolean jpaExistsById(Long clientId);

    boolean cassandraExistsById(Long clientId);

    void jpaSaveBenchmark(Benchmark benchmark);

    void jpaTruncateAll();

    void cassandraSaveBenchmark(Benchmark benchmark);

    void cassandraTruncateAll();
}
