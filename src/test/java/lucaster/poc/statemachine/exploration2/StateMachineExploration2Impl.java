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
        StateMachineProcessDefinition pd = StateMachineProcessDefinition.EXAMPLE_SM_PROCESS;
        ProcessInstance pi = new StateMachineProcessInstance(pd);
        StateMachineState activeState = ExampleSmProcessStates.STATE1;
		((StateMachineProcessInstance)pi).setActiveState(activeState);
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

class AppProcInst {
    final ProcessInstance pi;
    final String appInstanceId;
    AppProcInst(ProcessInstance pi, String appInstanceId /* + active state if FSM or active tasks if BPMN */) {
        this.pi = pi;
        this.appInstanceId = appInstanceId;
    }
}

class StateMachineProcessInstance implements ProcessInstance {

    private final StateMachineProcessDefinition pd;
    private StateMachineState activeState;

    StateMachineProcessInstance(StateMachineProcessDefinition pd) {
        this.pd = pd;
    }

    @Override
    public ProcessDefinition getProcessDefinition() {
        return pd;
    }

    @Override
    public Iterable<Task> getActiveTasks() {
        StateMachineState activeState = getActiveState();
        Iterable<Task> activeTasks = pd.getOutgoingTransitions(activeState);
        return activeTasks;
    }

    private StateMachineState getActiveState() {
        return activeState;
    }

    // Normally should only be set upon creation, or in AM
    void setActiveState(StateMachineState state) {
        this.activeState = state;
    }
}

interface StateMachineState {
	boolean isFinal();
}
interface StateMachineTransition {
	StateMachineState getFrom();
	StateMachineState getTo();
}


enum StateMachineProcessDefinition implements ProcessDefinition {

    EXAMPLE_SM_PROCESS(Utils.<ExampleSmProcessTransitions>toSet(
    	ExampleSmProcessTransitions.TASK1, 
    	ExampleSmProcessTransitions.TASK2
    )) {
        @Override
        public String getProcessId() {
            return "EXAMPLE_SM_PROCESS";
        }
    };

    protected final Iterable<ExampleSmProcessTransitions> tasks;

    StateMachineProcessDefinition(Iterable<ExampleSmProcessTransitions> tasks) {
        this.tasks = tasks;
    }

    public Iterable<Task> getOutgoingTransitions(StateMachineState state) {
    	Set<Task> set = new HashSet<>();
    	for (ExampleSmProcessTransitions t : getTasks()) {
    		if (state.equals(t.getFrom())) {
    			set.add(t);
    		}
    	}
    	return Collections.unmodifiableSet(set);
	}

	Set<StateMachineState> getStates() {
		Set<StateMachineState> set = new HashSet<>();
    	for (StateMachineTransition t : getTasks()) {
    		set.add(t.getFrom());
    		set.add(t.getTo());
    	}
    	return Collections.unmodifiableSet(set);
	}

	@Override
	public abstract String getProcessId();

    protected Iterable<ExampleSmProcessTransitions> getTasks() {
        return tasks;
    }

    @Override
	public Task findTaskByName(String taskName) {
        for (Task t : getTasks()) {
            if (t.getTaskName().equals(taskName)) {
                return t;
            }
        }
        return null;
    }
}
enum ExampleSmProcessTransitions implements Task, StateMachineTransition {
    TASK1(	"TASK1",
    		ExampleSmProcessStates.STATE1, 
    		ExampleSmProcessStates.STATE2, 
    		Utils.<ProcessRole>toSet(ExampleSmProcessRoles.ROLE1)
    ), 
    TASK2(	"TASK2",
    		ExampleSmProcessStates.STATE2, 
    		ExampleSmProcessStates.STATE3, 
    		Utils.<ProcessRole>toSet(ExampleSmProcessRoles.ROLE2)
    );
    protected final Iterable<ProcessRole> roles;
    protected final StateMachineState from;
    protected final StateMachineState to;
    protected final String name;
    ExampleSmProcessTransitions(String name, StateMachineState from, StateMachineState to, Iterable<ProcessRole> roles) {
    	this.name = name;
    	this.from = from;
    	this.to = to;
        this.roles = roles;
    }
    @Override public String getTaskName() {
        return name;
    }
    @Override public StateMachineState getFrom() {
    	return from;
    }
    @Override public StateMachineState getTo() {
    	return to;
    }
    @Override public Iterable<ProcessRole> getAllowedRoles() {
        return roles;
    }
}
enum ExampleSmProcessRoles implements ProcessRole {
    ROLE1,
    ROLE2;
}
enum ExampleSmProcessStates implements StateMachineState {
	STATE1(false),
	STATE2(false),
	STATE3(true);
	protected final boolean isFinal;
	ExampleSmProcessStates(boolean isFinal) {
		this.isFinal = isFinal;
	}
	@Override public boolean isFinal() {
		return isFinal;
	}
}