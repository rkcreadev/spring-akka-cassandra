package com.rkcreadev.demo.akka.model.db.cassandra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("client_info")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientInfoCassandra {
    @Id
    private Long clientId;
    @Column
    private Long spentTotal;
}
