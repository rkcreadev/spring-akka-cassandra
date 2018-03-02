package com.rkcreadev.demo.akka.model.db.jpa;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class SubscriberJpa {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Long subscriberId;
    @Column
    private Long spent;
}
