package com.rest.api;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile(value = "test")
@Component
@Primary
public class InMemoryPaymentStore implements PaymentStoreService {

    private Map<String, List<Payment>> paymentMap = new HashMap<>();

    @Override
    public void store(Payment payment) {
        if (paymentMap.get(payment.getPaymentId()) == null) {
            paymentMap.put(payment.getPaymentId(), Arrays.asList(payment));
        } else {
            paymentMap.get(payment.getPaymentId()).add(payment);
        }
    }

    @Override
    public List<Payment> getPayment(String paymentId) {
        return paymentMap.get(paymentId);
    }
}
