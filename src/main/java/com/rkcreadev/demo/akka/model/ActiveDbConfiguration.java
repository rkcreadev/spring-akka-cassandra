package com.rkcreadev.demo.akka.model;

import com.rkcreadev.demo.akka.model.db.common.Benchmark;
import com.rkcreadev.demo.akka.model.db.common.ClientInfo;
import com.rkcreadev.demo.akka.service.DbVisitor;

public enum ActiveDbConfiguration {
    JPA {
        @Override
        public void save(DbVisitor dbVisitor, ClientInfo clientInfo) {
            dbVisitor.jpaSave(clientInfo);
        }

        @Override
        public boolean existsById(DbVisitor dbVisitor, Long clientId) {
            return dbVisitor.jpaExistsById(clientId);
        }

        @Override
        public void saveBenchmark(DbVisitor dbVisitor, Benchmark benchmark) {
            dbVisitor.jpaSaveBenchmark(benchmark);
        }

        @Override
        public void truncateAll(DbVisitor dbVisitor) {
            dbVisitor.jpaTruncateAll();
        }
    },
    CASSANDRA {
        @Override
        public void save(DbVisitor dbVisitor, ClientInfo clientInfo) {
            dbVisitor.cassandraSave(clientInfo);
        }

        @Override
        public boolean existsById(DbVisitor dbVisitor, Long clientId) {
            return dbVisitor.cassandraExistsById(clientId);
        }

        @Override
        public void saveBenchmark(DbVisitor dbVisitor, Benchmark benchmark) {
            dbVisitor.cassandraSaveBenchmark(benchmark);
        }

        @Override
        public void truncateAll(DbVisitor dbVisitor) {
            dbVisitor.cassandraTruncateAll();
        }
    };

    public abstract void save(DbVisitor dbVisitor, ClientInfo clientInfo);

    public abstract boolean existsById(DbVisitor dbVisitor, Long clientId);

    public abstract void saveBenchmark(DbVisitor dbVisitor, Benchmark benchmark);

    public abstract void truncateAll(DbVisitor dbVisitor);
}
