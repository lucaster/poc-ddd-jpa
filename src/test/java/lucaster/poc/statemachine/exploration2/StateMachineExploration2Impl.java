package lucaster.poc.statemachine.exploration2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ExampleUsher extends AbstractUsher {

    public ExampleUsher(	UsherProcessIntegrationQuery procIntegrQuery, 
                            UsherProcessTopologyQuery procTopoQuery,
                            UsherRoleQuery roleQuery) {
        super(procIntegrQuery, procTopoQuery, roleQuery);
    }

    @Override
    boolean isTheActivePersonForTaskOfInstance(String username, String taskName, String appInstanceId) {
    	// Leverage: username is the owner of appInstanceId
        return true;
    }   
}

class ExampleUsherProcessIntegrationQuery implements UsherProcessIntegrationQuery {

    // Real implementation would fetch from a db via thrugh repository pattern
    private final Set<AppProcInst> procInstRepo;

    ExampleUsherProcessIntegrationQuery() {
        procInstRepo = new HashSet<>();
        StateMachineDefinitions pd = StateMachineDefinitions.EXAMPLE_SM_PROCESS;
        ProcessInstance pi = new StateMachineInstance(pd);
        StateMachineState activeState = ExampleSmProcessStates.STATE1;
		((StateMachineInstance)pi).setActiveState(activeState);
        String appInstanceId = "proposalId123";
        AppProcInst appProcInst = new AppProcInst(pi, appInstanceId);
        procInstRepo.add(appProcInst);
    }

    @Override
    public ProcessInstance findProcessInstance(String appInstanceId) {
        for (AppProcInst api : procInstRepo) {
            if (appInstanceId.equals(api.appInstanceId)) {
                return api.pi;
            }
        }
        return null;
    }
}

class ExampleUsherRoleQuery implements UsherRoleQuery {

    @Override
    public Iterable<ProcessRole> findRoles(String username) {
        // username -> bank profiles
        // bank profiles -> Example process roles
        Set<ProcessRole> roles = new HashSet<>();
        roles.add(ExampleSmProcessRoles.ROLE1);
        return Collections.unmodifiableSet(roles);
    }

}



