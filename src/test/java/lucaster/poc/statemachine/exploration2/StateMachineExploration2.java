package lucaster.poc.statemachine.exploration2;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StateMachineExploration2 {

    @Test
    void explore() {
        Usher usher = new LeverageUsher();
        boolean can = usher.canExecute("EE53414", "TASK1", "proposalId123");
        assertTrue(can);
    }
}
