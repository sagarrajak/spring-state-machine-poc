package sagar.springproject.springstatemachinepoc.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import sagar.springproject.springstatemachinepoc.domain.Payment;
import sagar.springproject.springstatemachinepoc.domain.PaymentEvent;
import sagar.springproject.springstatemachinepoc.domain.PaymentState;
import sagar.springproject.springstatemachinepoc.repository.PaymentRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentStateChangeIntercepter extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {
    private final PaymentRepository paymentRepository;

    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message, Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState, PaymentEvent> stateMachine, StateMachine<PaymentState, PaymentEvent> rootStateMachine) {
        Optional.ofNullable(message).ifPresent(mgs -> {
            Long cast = Long.class.cast(mgs.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER, -1L));
            Optional.ofNullable(cast).ifPresent(paymentId -> {
                Payment payment = paymentRepository.getReferenceById(paymentId);
                payment.setState(state.getId());
                paymentRepository.save(payment);
            });
        });
    }
}
