package com.rkcreadev.demo.akka.model.db.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.Set;

@Table(name = "client_info")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientInfoJpa {
    @Id
    private Long clientId;
    @Column
    private Long spentTotal;

    @ElementCollection
    @CollectionTable(name = "subscriber_ids", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "subscriber")
    private Set<Long> subscribers;
}
