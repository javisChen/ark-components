package com.ark.component.statemachine.cola;

import com.ark.component.cola.statemachine.Action;
import com.ark.component.cola.statemachine.Condition;
import com.ark.component.cola.statemachine.StateMachine;
import com.ark.component.cola.statemachine.StateMachineFactory;
import com.ark.component.cola.statemachine.builder.StateMachineBuilder;
import com.ark.component.cola.statemachine.builder.StateMachineBuilderFactory;
import com.ark.component.cola.statemachine.exception.TransitionFailException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * StateMachineTest
 *
 * @author Frank Zhang
 * @date 2020-02-08 12:19 PM
 */
public class StateMachineTest {

    static String MACHINE_ID = "TestStateMachine";

    static enum States {
        STATE1,
        STATE2,
        STATE3,
        STATE4
    }

    static enum Events {
        EVENT1,
        EVENT2,
        EVENT3,
        EVENT4,
        INTERNAL_EVENT
    }

    static class Context {
        String operator = "frank";
        String entityId = "123465";
    }

    @Test
    public void testExternalNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
            .from(States.STATE1)
            .to(States.STATE2)
            .on(Events.EVENT1)
            .when(checkCondition())
            .perform(doAction());
        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID);
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
        Assertions.assertEquals(States.STATE2, target);
    }

    @Test
    public void testFail() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
            .from(States.STATE1)
            .to(States.STATE2)
            .on(Events.EVENT1)
            .when(checkCondition())
            .perform(doAction())
        ;


        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID + "-testFail");
        Assertions.assertThrows(TransitionFailException.class,
            () -> stateMachine.fireEvent(States.STATE2, Events.EVENT1, new Context()));
    }

    @Test
    public void testVerify() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
            .from(States.STATE1)
            .to(States.STATE2)
            .on(Events.EVENT1)
            .when(checkCondition())
            .perform(doAction());

        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID + "-testVerify");

        Assertions.assertTrue(stateMachine.verify(States.STATE1, Events.EVENT1));
        Assertions.assertFalse(stateMachine.verify(States.STATE1, Events.EVENT2));
    }

    @Test
    public void testExternalTransitionsNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransitions()
            .fromAmong(States.STATE1, States.STATE2, States.STATE3)
            .to(States.STATE4)
            .on(Events.EVENT1)
            .when(checkCondition())
            .perform(doAction());

        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID + "1");
        States target = stateMachine.fireEvent(States.STATE2, Events.EVENT1, new Context());
        Assertions.assertEquals(States.STATE4, target);
    }

    @Test
    public void testInternalNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.internalTransition()
            .within(States.STATE1)
            .on(Events.INTERNAL_EVENT)
            .when(checkCondition())
            .perform(doAction());
        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID + "2");

        stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
        States target = stateMachine.fireEvent(States.STATE1, Events.INTERNAL_EVENT, new Context());
        Assertions.assertEquals(States.STATE1, target);
    }

    @Test
    public void testExternalInternalNormal() {
        StateMachine<States, Events, Context> stateMachine = buildStateMachine("testExternalInternalNormal");

        Context context = new Context();
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, context);
        Assertions.assertEquals(States.STATE2, target);
        target = stateMachine.fireEvent(States.STATE2, Events.INTERNAL_EVENT, context);
        Assertions.assertEquals(States.STATE2, target);
        target = stateMachine.fireEvent(States.STATE2, Events.EVENT2, context);
        Assertions.assertEquals(States.STATE1, target);
        target = stateMachine.fireEvent(States.STATE1, Events.EVENT3, context);
        Assertions.assertEquals(States.STATE3, target);
    }

    private StateMachine<States, Events, Context> buildStateMachine(String machineId) {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
            .from(States.STATE1)
            .to(States.STATE2)
            .on(Events.EVENT1)
            .when(checkCondition())
            .perform(doAction());

        builder.internalTransition()
            .within(States.STATE2)
            .on(Events.INTERNAL_EVENT)
            .when(checkCondition())
            .perform(doAction());

        builder.externalTransition()
            .from(States.STATE2)
            .to(States.STATE1)
            .on(Events.EVENT2)
            .when(checkCondition())
            .perform(doAction());

        builder.externalTransition()
            .from(States.STATE1)
            .to(States.STATE3)
            .on(Events.EVENT3)
            .when(checkCondition())
            .perform(doAction());

        builder.externalTransitions()
            .fromAmong(States.STATE1, States.STATE2, States.STATE3)
            .to(States.STATE4)
            .on(Events.EVENT4)
            .when(checkCondition())
            .perform(doAction());

        builder.build(machineId);

        StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get(machineId);
        stateMachine.showStateMachine();
        return stateMachine;
    }

    @Test
    public void testMultiThread() {
        buildStateMachine("testMultiThread");

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
                Assertions.assertEquals(States.STATE2, target);
            });
            thread.start();
        }

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT4, new Context());
                Assertions.assertEquals(States.STATE4, target);
            });
            thread.start();
        }

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT3, new Context());
                Assertions.assertEquals(States.STATE3, target);
            });
            thread.start();
        }

    }

    private Condition<Context> checkCondition() {
        return new Condition<Context>() {
            @Override
            public boolean isSatisfied(Context context) {
                System.out.println("Check condition : " + context);
                return true;
            }
        };
    }

    private Action<States, Events, Context> doAction() {
        return (from, to, event, ctx) -> {
            System.out.println(
                ctx.operator + " is operating " + ctx.entityId + " from:" + from + " to:" + to + " on:" + event);
        };
    }

}
