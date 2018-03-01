package com.rkcreadev.demo.akka.model.db.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfo {
    private Long clientId;
    private Long spentTotal;
    private Set<Long> subscribers;
}
