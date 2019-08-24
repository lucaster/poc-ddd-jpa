package lucaster.poc.statemachine.exploration2;

import org.junit.Test;

public class StateMachineExploration2 {

    @Test
    void explore() {
        Usher usher = new LeverageUsher();
        boolean can = usher.canExecute("EE53414", "EXAMPLE_SM_PROCESS", "proposalId123");
    }
}
