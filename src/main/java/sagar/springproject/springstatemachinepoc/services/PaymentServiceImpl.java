package sagar.springproject.springstatemachinepoc.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import sagar.springproject.springstatemachinepoc.domain.Payment;
import sagar.springproject.springstatemachinepoc.domain.PaymentEvent;
import sagar.springproject.springstatemachinepoc.domain.PaymentState;
import sagar.springproject.springstatemachinepoc.repository.PaymentRepository;

import javax.transaction.Transactional;


@Data
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    public static final String PAYMENT_ID_HEADER = "payment-id";
    private final PaymentRepository paymentRepository;
    private final StateMachineFactory<PaymentState, PaymentEvent> paymentStateMachine;

    private final PaymentStateChangeIntercepter paymentStateChangeIntercepter;

    @Transactional
    @Override
    public Payment newPayment(Payment payment) {
        payment.setState(PaymentState.NEW);
        return paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm ,PaymentEvent.PRE_AUTH_APPROVED);
        return sm;
    }

    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm ,PaymentEvent.AUTH_APPROVED);
        return sm;
    }

    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm ,PaymentEvent.AUTH_DECLINED);
        return sm;
    }

    private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> stateMachine, PaymentEvent event) {
        Message<PaymentEvent> message = MessageBuilder.withPayload(event).setHeader(PAYMENT_ID_HEADER, paymentId).build();
        stateMachine.sendEvent(message);
    }

    private StateMachine<PaymentState, PaymentEvent> build(Long paymentId) {
        Payment payment = paymentRepository.getReferenceById(paymentId);
        StateMachine<PaymentState, PaymentEvent> sm = paymentStateMachine.getStateMachine(Long.toString(paymentId));
        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(paymentStateChangeIntercepter);
            sma.resetStateMachine(new DefaultStateMachineContext<>(payment.getState(), null, null, null));
        });
        sm.start();
        return sm;
    }
}
