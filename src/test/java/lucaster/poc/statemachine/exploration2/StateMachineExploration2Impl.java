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
    private final Set<StateMachineSimpleIntegration> simpleRepo;

    ExampleUsherProcessIntegrationQuery() {
        procInstRepo = new HashSet<>();
        StateMachineDefinitions pd = StateMachineDefinitions.EXAMPLE_SM_PROCESS;
		StateMachineInstance pi = new StateMachineInstance(pd);
        pi.setActiveState(ExampleSmProcessStates.STATE1);
        String appInstanceId = "proposalId123";
        AppProcInst appProcInst = new AppProcInst(pi, appInstanceId);
        procInstRepo.add(appProcInst);
        
        simpleRepo = new HashSet<>();
        simpleRepo.add(
        	new StateMachineSimpleIntegration(
        			appInstanceId, 
        			pi.getProcessInstanceId(), 
        			pd.getProcessDefinitionId(), 
        			ExampleSmProcessStates.STATE1.getName()
        	)
        );
    }

    // Real implementation would fetch from a db via thrugh repository pattern
    @Override
    public ProcessInstance findProcessInstance(String appInstanceId) {
        ProcessInstance fromProcInstRepo = findProcessInstanceFromProcInstRepo(appInstanceId);
        ProcessInstance fromSimple = findProcessInstanceFromProcSimpleRepo(appInstanceId);
		return fromSimple;
    }

	private ProcessInstance findProcessInstanceFromProcInstRepo(String appInstanceId) {
		for (AppProcInst api : procInstRepo) {
            if (appInstanceId.equals(api.appInstanceId)) {
                return api.pi;
            }
        }
        return null;
	}
	
	private ProcessInstance findProcessInstanceFromProcSimpleRepo(String appInstanceId) {
		for (StateMachineSimpleIntegration api : simpleRepo) {
            if (appInstanceId.equals(api.appInstanceId)) {
            	StateMachineDefinitions pd = StateMachineDefinitions.valueOf(api.processDefinitionId);
            	StateMachineInstance instance = new StateMachineInstance(pd);
            	StateMachineState activeState = findStateMachineState(pd, api.activeStateName);
                instance.setActiveState(activeState);
                return instance;
            }
        }
        return null;
	}

	private StateMachineState findStateMachineState(StateMachineDefinitions pd, String activeStateName) {
		Set<StateMachineState> states = pd.getStates();
		for(StateMachineState state : states) {
			if (state.getName().equals(activeStateName)) {
				return state;
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



