package com.rkcreadev.demo.akka.service;

import com.rkcreadev.demo.akka.model.db.common.Benchmark;

public interface BenchmarkService {

    void save(Benchmark benchmark);
}
