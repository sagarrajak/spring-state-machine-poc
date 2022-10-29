package sagar.springproject.springstatemachinepoc.services;

import org.springframework.statemachine.StateMachine;
import sagar.springproject.springstatemachinepoc.domain.Payment;
import sagar.springproject.springstatemachinepoc.domain.PaymentEvent;
import sagar.springproject.springstatemachinepoc.domain.PaymentState;

public interface PaymentService {
    Payment newPayment(Payment payment);

    StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);

    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);

    StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);
}
