package com.rkcreadev.demo.akka.model.db.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name = "client_subscriber",
            joinColumns = {@JoinColumn(name = "client_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id")}
    )
    private List<SubscriberJpa> subscribers;
}
