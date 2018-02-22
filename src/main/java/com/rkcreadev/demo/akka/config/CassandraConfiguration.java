package com.rkcreadev.demo.akka.config;

import com.rkcreadev.demo.akka.model.db.ClientInfo;
import com.rkcreadev.demo.akka.repository.ClientInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackageClasses = ClientInfoRepository.class)
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Value("${cassandra.keyspace.name}")
    private String keySpaceName;
    @Value("${cassandra.port}")
    private int port;
    @Value("${cassandra.contactPoints}")
    private String contactPoints;

    @Override
    protected String getKeyspaceName() {
        return keySpaceName;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {ClientInfo.class.getPackage().getName()};
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }
}
