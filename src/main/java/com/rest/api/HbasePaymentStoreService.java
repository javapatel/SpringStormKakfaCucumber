package com.rest.api;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Profile(value = "!test")
public class HbasePaymentStoreService implements PaymentStoreService {
    @Override
    public void store(Payment payment) {

    }

    @Override
    public List<Payment> getPayment(String paymentId) {
        return Arrays.asList(new Payment());
    }
}
