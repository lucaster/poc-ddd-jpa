package lucaster.poc.statemachine.exploration2;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * Implementations of these must be redone for the project
 */

/**
 * For data-driven.
 */
class TestDataDrivenIntegrationRepository implements IntegrationRepository {
	private static final Set<StateMachineDataDrivenIntegration> procInstRepo;
	static {
		procInstRepo = new HashSet<>();
	    StateMachineDefinitions pd = StateMachineDefinitions.EXAMPLE_SM_PROCESS;
		StateMachineInstance pi = new StateMachineInstance(pd);
	    pi.setActiveState(ExampleSmProcessStates.STATE1);
	    String appInstanceId = "proposalId123";
	    StateMachineDataDrivenIntegration appProcInst = new StateMachineDataDrivenIntegration(pi, appInstanceId);
	    procInstRepo.add(appProcInst);
	}
	@Override public ProcessInstance findProcessInstanceByAppIntanceId(String appInstanceId) {
		for (StateMachineDataDrivenIntegration api : procInstRepo) {
            if (appInstanceId.equals(api.appInstanceId)) {
                return api.pi;
            }
        }
        return null;
	}
}

/**
 * For not data-driven
 */
class TestSimpleIntegrationRepository implements IntegrationRepository {
	private static final Set<StateMachineSimpleIntegration> repo;
	static {
		repo = new HashSet<>();
		String appInstanceId = "proposalId123";
		StateMachineDefinitions pd = StateMachineDefinitions.EXAMPLE_SM_PROCESS;
		StateMachineInstance pi = new StateMachineInstance(pd);
        repo.add(
        	new StateMachineSimpleIntegration(
        			appInstanceId, 
        			pi.getProcessInstanceId(), 
        			pd.getProcessDefinitionId(), 
        			ExampleSmProcessStates.STATE1.getName()
        	)
        );
	}
	@Override public ProcessInstance findProcessInstanceByAppIntanceId(String appInstanceId) {
		for (StateMachineSimpleIntegration api : repo) {
            if (appInstanceId.equals(api.appInstanceId)) {
            	StateMachineDefinitions pd = StateMachineDefinitions.valueOf(api.processDefinitionId);
            	StateMachineInstance instance = new StateMachineInstance(api.processInstanceId, pd);
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

class TestProcessRoleRepository implements ProcessRoleRepository {
	private static final Map<String, Iterable<ProcessRole>> repo;
	static {
		repo = new HashMap<>();
		repo.put("EE53414", Utils.<ProcessRole>toSet(ExampleSmProcessRoles.ROLE1));
		repo.put("EE37987", Utils.<ProcessRole>toSet(ExampleSmProcessRoles.ROLE2));
	}
	@Override public Iterable<ProcessRole> findRoles(String username) {
		Iterable<ProcessRole> roles = repo.get(username);
		@SuppressWarnings("unchecked")
		Iterable<ProcessRole> result = (Iterable<ProcessRole>) (roles != null ? roles : Collections.emptySet());
		return result;
	}	
}

class TestUsherImpl extends AbstractUsher {
    public TestUsherImpl(
    	UsherProcessIntegrationQuery procIntegrQuery, 
    	UsherProcessTopologyQuery procTopoQuery,
    	UsherRoleQuery roleQuery
    ) {
        super(procIntegrQuery, procTopoQuery, roleQuery);
    }
    @Override boolean isTheActivePersonForTaskOfInstance(String username, String taskName, String appInstanceId) {
    	// Leverage: username is the owner of appInstanceId
        return true;
    }   
}