package com.rest.api;

import java.util.List;

public interface PaymentStoreService {
    void store(Payment payment);

    List<Payment> getPayment(String paymentId);
}
