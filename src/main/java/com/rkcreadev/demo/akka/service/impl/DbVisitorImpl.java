package com.rkcreadev.demo.akka.service.impl;

import com.rkcreadev.demo.akka.mapping.ClientInfoMapper;
import com.rkcreadev.demo.akka.model.db.common.ClientInfo;
import com.rkcreadev.demo.akka.repository.cassandra.CassandraClientInfoRepository;
import com.rkcreadev.demo.akka.repository.jpa.JpaClientInfoRepository;
import com.rkcreadev.demo.akka.service.DbVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DbVisitorImpl implements DbVisitor {

    private JpaClientInfoRepository jpaClientInfoRepository;
    private CassandraClientInfoRepository cassandraClientInfoRepository;
    private ClientInfoMapper clientInfoMapper;

    @Autowired
    public DbVisitorImpl(JpaClientInfoRepository jpaClientInfoRepository,
                         CassandraClientInfoRepository cassandraClientInfoRepository,
                         ClientInfoMapper clientInfoMapper) {
        this.jpaClientInfoRepository = jpaClientInfoRepository;
        this.cassandraClientInfoRepository = cassandraClientInfoRepository;
        this.clientInfoMapper = clientInfoMapper;
    }

    @Transactional
    @Override
    public void jpaSave(ClientInfo clientInfo) {
        jpaClientInfoRepository.saveAndFlush(clientInfoMapper.toJpaEntity(clientInfo));
    }

    @Transactional
    @Override
    public boolean jpaExistsById(Long clientId) {
        return jpaClientInfoRepository.existsById(clientId);
    }

    @Override
    public void cassandraSave(ClientInfo clientInfo) {
        cassandraClientInfoRepository.save(clientInfoMapper.toCassandraEntity(clientInfo));
    }

    @Override
    public boolean cassandraExistsById(Long clientId) {
        return cassandraClientInfoRepository.existsById(clientId);
    }
}
