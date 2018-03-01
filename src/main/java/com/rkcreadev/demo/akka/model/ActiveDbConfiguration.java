package com.rkcreadev.demo.akka.model;

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
    };

    public abstract void save(DbVisitor dbVisitor, ClientInfo clientInfo);

    public abstract boolean existsById(DbVisitor dbVisitor, Long clientId);
}
