package com.rkcreadev.demo.akka.model.db.jpa;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "benchmark")
@Entity
@Data
public class JpaBenchmark {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String runId;

    @Column
    private Long filesCount;

    @Column
    private Long milliseconds;

    @Column
    private Date startDate;
}
