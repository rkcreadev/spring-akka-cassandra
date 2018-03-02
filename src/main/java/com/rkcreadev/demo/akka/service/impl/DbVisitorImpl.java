package com.rkcreadev.demo.akka.service.impl;

import com.rkcreadev.demo.akka.mapping.BenchmarkMapper;
import com.rkcreadev.demo.akka.mapping.ClientInfoMapper;
import com.rkcreadev.demo.akka.mapping.SubscriberMapper;
import com.rkcreadev.demo.akka.model.db.cassandra.SubscriberCassandra;
import com.rkcreadev.demo.akka.model.db.common.Benchmark;
import com.rkcreadev.demo.akka.model.db.common.ClientInfo;
import com.rkcreadev.demo.akka.repository.cassandra.CassandraBenchmarkRepository;
import com.rkcreadev.demo.akka.repository.cassandra.CassandraClientInfoRepository;
import com.rkcreadev.demo.akka.repository.cassandra.CassandraSubscriberRepository;
import com.rkcreadev.demo.akka.repository.jpa.JpaBenchmarkRepository;
import com.rkcreadev.demo.akka.repository.jpa.JpaClientInfoRepository;
import com.rkcreadev.demo.akka.service.DbVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DbVisitorImpl implements DbVisitor {

    private JpaClientInfoRepository jpaClientInfoRepository;
    private CassandraClientInfoRepository cassandraClientInfoRepository;
    private JpaBenchmarkRepository jpaBenchmarkRepository;
    private ClientInfoMapper clientInfoMapper;
    private BenchmarkMapper benchmarkMapper;
    private SubscriberMapper subscriberMapper;
    private CassandraBenchmarkRepository cassandraBenchmarkRepository;
    private CassandraSubscriberRepository cassandraSubscriberRepository;

    @Autowired
    public DbVisitorImpl(JpaClientInfoRepository jpaClientInfoRepository,
                         CassandraClientInfoRepository cassandraClientInfoRepository,
                         JpaBenchmarkRepository jpaBenchmarkRepository, ClientInfoMapper clientInfoMapper,
                         BenchmarkMapper benchmarkMapper, SubscriberMapper subscriberMapper,
                         CassandraBenchmarkRepository cassandraBenchmarkRepository,
                         CassandraSubscriberRepository cassandraSubscriberRepository) {
        this.jpaClientInfoRepository = jpaClientInfoRepository;
        this.cassandraClientInfoRepository = cassandraClientInfoRepository;
        this.jpaBenchmarkRepository = jpaBenchmarkRepository;
        this.clientInfoMapper = clientInfoMapper;
        this.benchmarkMapper = benchmarkMapper;
        this.subscriberMapper = subscriberMapper;
        this.cassandraBenchmarkRepository = cassandraBenchmarkRepository;
        this.cassandraSubscriberRepository = cassandraSubscriberRepository;
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

    @Transactional
    @Override
    public void jpaSaveBenchmark(Benchmark benchmark) {
        jpaBenchmarkRepository.saveAndFlush(benchmarkMapper.toJpaEntity(benchmark));
    }

    @Override
    public void jpaTruncateAll() {
        jpaClientInfoRepository.deleteAll();
    }

    @Override
    public void cassandraSave(ClientInfo clientInfo) {
        cassandraClientInfoRepository.save(clientInfoMapper.toCassandraEntity(clientInfo));
        List<SubscriberCassandra> subscribers = clientInfo.getSubscribers()
                .stream()
                .map(subscriberMapper::toCassandraEntity)
                .collect(Collectors.toList());
        cassandraSubscriberRepository.saveAll(subscribers);
    }

    @Override
    public boolean cassandraExistsById(Long clientId) {
        return cassandraClientInfoRepository.existsById(clientId);
    }

    @Override
    public void cassandraSaveBenchmark(Benchmark benchmark) {
        cassandraBenchmarkRepository.save(benchmarkMapper.toCassandraEntity(benchmark));
    }

    @Override
    public void cassandraTruncateAll() {
        cassandraClientInfoRepository.deleteAll();
    }
}
