package com.rkcreadev.demo.akka.model.db.common;

import lombok.Data;

import java.util.Date;

@Data
public class Benchmark {
    private String runId;
    private Long filesCount;
    private Long milliseconds;
    private Date startDate;
}
