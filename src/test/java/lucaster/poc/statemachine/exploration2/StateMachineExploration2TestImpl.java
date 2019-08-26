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
class TestDataDrivenStateMachineIntegrationRepository implements ProcessIntegrationRepository {
	private final Set<StateMachineDataDrivenIntegration> repo;
	{
		repo = new HashSet<>();
	    StateMachineProcessDefinition pd = StateMachineProcessDefinition.EXAMPLE_SM_PROCESS;
		StateMachineInstance pi = new StateMachineInstance(pd, ExampleSmStates.STATE1);
	    String appInstanceId = "proposalId123";
	    StateMachineDataDrivenIntegration appProcInst = new StateMachineDataDrivenIntegration(pi, appInstanceId);
	    repo.add(appProcInst);
	}
	@Override public ProcessInstance findProcessInstanceByAppInstanceId(String appInstanceId) {
		for (StateMachineDataDrivenIntegration api : repo) {
            if (appInstanceId.equals(api.appInstanceId)) {
                return api.pi;
            }
        }
        return null;
	}
	@Override
	public void updateIntegrationInfo(ProcessInstance pi, String appInstanceId) {
		for (StateMachineDataDrivenIntegration api : repo) {
            if (appInstanceId.equals(api.appInstanceId) && pi.equals(api.pi)) {
            	StateMachineInstance incoming = (StateMachineInstance) pi;
            	StateMachineInstance stored = (StateMachineInstance) api.pi;
                stored.setActiveState(incoming.getActiveState());
            }
        }
	}
	@Override
	public void addIntegrationInfo(ProcessInstance pi, String appInstanceId) {
		for (StateMachineDataDrivenIntegration api : repo) {
            if (appInstanceId.equals(api.appInstanceId) || pi.equals(api.pi)) {
            	throw new RuntimeException("App instance id already present");
            }
        }
		repo.add(new StateMachineDataDrivenIntegration(pi, appInstanceId));
	}
}

/**
 * For not data-driven
 */
class TestSimpleStateMachineIntegrationRepository implements ProcessIntegrationRepository {
	private final ProcessTopologyQuery topoQuery;
	TestSimpleStateMachineIntegrationRepository(ProcessTopologyQuery topoQuery) {
		this.topoQuery = topoQuery;
	}
	private final Set<StateMachineSimpleIntegration> repo;
	{
		repo = new HashSet<>();
		String appInstanceId = "proposalId123";
		StateMachineProcessDefinition pd = StateMachineProcessDefinition.EXAMPLE_SM_PROCESS;
		StateMachineInstance pi = new StateMachineInstance(pd);
        repo.add(
        	new StateMachineSimpleIntegration(
        			appInstanceId, 
        			pi.getProcessInstanceId(), 
        			pd.getProcessDefinitionId(), 
        			ExampleSmStates.STATE1.getName()
        	)
        );
	}
	@Override public ProcessInstance findProcessInstanceByAppInstanceId(String appInstanceId) {
		for (StateMachineSimpleIntegration api : repo) {
            if (appInstanceId.equals(api.appInstanceId)) {
            	StateMachineProcessDefinition pd = StateMachineProcessDefinition.valueOf(api.processDefinitionId);
            	StateMachineState activeState = findStateMachineState(pd, api.getActiveStateName());
            	StateMachineInstance instance = new StateMachineInstance(api.processInstanceId, pd, activeState);
                return instance;
            }
        }
        return null;
	}
	private StateMachineState findStateMachineState(StateMachineProcessDefinition pd, String activeStateName) {
		Set<StateMachineState> states = pd.getStates();
		for(StateMachineState state : states) {
			if (state.getName().equals(activeStateName)) {
				return state;
			}
		}
		return null;
	}
	@Override
	public void updateIntegrationInfo(ProcessInstance pi, String appInstanceId) {
		for (StateMachineSimpleIntegration api : repo) {
			if (appInstanceId.equals(api.appInstanceId)) {
				StateMachineInstance smi = (StateMachineInstance) pi;
				api.setActiveState(smi.getActiveState());
			}
		}
	}
	@Override
	public void addIntegrationInfo(ProcessInstance pi, String appInstanceId) {
		for (StateMachineSimpleIntegration api : repo) {
			if (appInstanceId.equals(api.appInstanceId) || pi.getProcessInstanceId().equals(api.processInstanceId)) {
				throw new RuntimeException("App instance id already present");
			}
		}
		String processInstanceId = pi.getProcessInstanceId();
		ProcessDefinition pd = topoQuery.findProcessDefinition(pi);
		String processDefinitionId = pd.getProcessDefinitionId();
		StateMachineInstance smi = (StateMachineInstance) pi;
		StateMachineState activeState = smi.getActiveState();
		String activeStateName = activeState.getName();
		StateMachineSimpleIntegration api = new StateMachineSimpleIntegration(appInstanceId, processInstanceId, processDefinitionId, activeStateName);
		repo.add(api);
	}
}

class TestProcessRoleRepository implements ProcessRoleRepository {
	private final Map<String, Iterable<ProcessRole>> repo;
	{
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
    	ProcessTopologyQuery procTopoQuery,
    	UsherRoleQuery roleQuery
    ) {
        super(procIntegrQuery, procTopoQuery, roleQuery);
    }
    @Override boolean isTheActivePersonForTaskOfInstance(String username, String taskName, String appInstanceId) {
    	// Leverage: username is the owner of appInstanceId
        return true;
    }   
}