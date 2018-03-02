package com.rkcreadev.demo.akka.service.impl;

import com.rkcreadev.demo.akka.model.ActiveDbConfiguration;
import com.rkcreadev.demo.akka.model.db.common.Benchmark;
import com.rkcreadev.demo.akka.service.BenchmarkService;
import com.rkcreadev.demo.akka.service.DbVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BenchmarkServiceImpl implements BenchmarkService {

    private DbVisitor dbVisitor;

    @Value("${db.active}")
    private ActiveDbConfiguration activeDbConfiguration;

    @Autowired
    public BenchmarkServiceImpl(DbVisitor dbVisitor) {
        this.dbVisitor = dbVisitor;
    }

    @Override
    public void save(Benchmark benchmark) {
        activeDbConfiguration.saveBenchmark(dbVisitor, benchmark);
    }
}
