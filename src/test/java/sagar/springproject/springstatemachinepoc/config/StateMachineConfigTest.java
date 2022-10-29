package sagar.springproject.springstatemachinepoc.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import sagar.springproject.springstatemachinepoc.domain.PaymentEvent;
import sagar.springproject.springstatemachinepoc.domain.PaymentState;


@SpringBootTest
class StateMachineConfigTest {
    @Autowired
    StateMachineFactory<PaymentState, PaymentEvent> smf;

    @Test
    void tesStateMachine() {
        StateMachine<PaymentState, PaymentEvent> sm = smf.getStateMachine();
        sm.start();
        System.out.println(sm.getState().toString());
        sm.sendEvent(PaymentEvent.PRE_AUTHORIZE);
        System.out.println("Should be pre-auth approved or pre-auth error!");
        System.out.println(sm.getState().toString());
    }
}