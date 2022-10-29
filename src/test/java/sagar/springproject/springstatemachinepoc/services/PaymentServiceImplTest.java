package sagar.springproject.springstatemachinepoc.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sagar.springproject.springstatemachinepoc.domain.Payment;
import sagar.springproject.springstatemachinepoc.repository.PaymentRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {
    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Transactional
    @Test
    void preAuth() {
        Payment payment1 = paymentService.newPayment(payment);
        paymentService.preAuth(payment1.getId());
        Payment savedPayment = paymentRepository.getReferenceById(payment1.getId());
        System.out.println(savedPayment);
    }

}