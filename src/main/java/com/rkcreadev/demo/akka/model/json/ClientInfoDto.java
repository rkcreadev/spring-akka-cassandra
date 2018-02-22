package com.rkcreadev.demo.akka.model.json;

import lombok.Data;

@Data
public class ClientInfoDto {
    private Long clientId;
    private Long spentTotal;
    private boolean isBig;
}
