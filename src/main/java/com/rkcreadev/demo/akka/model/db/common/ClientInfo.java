package com.rkcreadev.demo.akka.model.db.common;

import com.rkcreadev.demo.akka.model.json.SubscriberPayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfo {
    private Long clientId;
    private Long spentTotal;
    private List<SubscriberPayment> subscribers;
}
