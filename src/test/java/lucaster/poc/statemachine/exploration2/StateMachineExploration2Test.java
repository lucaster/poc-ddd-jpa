package lucaster.poc.statemachine.exploration2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class StateMachineExploration2Test {

	ProcessRoleRepository processRoleRepository;
	ProcessIntegrationRepository integrationRepository;
	ProcessDefinitionRepository processDefinitionRepository;
	UsherProcessIntegrationQuery procIntegrQuery;
	ProcessTopologyQuery procTopoQuery;
	UsherRoleQuery roleQuery;
	Usher usher;
	ExecutorQuery executorQuery;
	Executor executor;

	String username;
	String taskName;
	String appInstanceId;

	@Before
	public void setup() {
		processRoleRepository = new TestProcessRoleRepository();
		integrationRepository = new TestSimpleStateMachineIntegrationRepository();
		processDefinitionRepository = new EnumDrivenProcessDefinitionRepository();
		procIntegrQuery = new UsherProcessIntegrationQueryImpl(integrationRepository);
		procTopoQuery = new UsherProcessTopologyQueryImpl(processDefinitionRepository);
		roleQuery = new UsherRoleQueryImpl(processRoleRepository);
		usher = new TestUsherImpl(procIntegrQuery, procTopoQuery, roleQuery);
		executorQuery = new ExecutorQueryImpl(integrationRepository, procTopoQuery);
		executor = new ExecutorImpl(executorQuery, integrationRepository);

		username = "EE53414";
		taskName = ExampleSmProcessTransitions.TASK1.getTaskName();
		appInstanceId = "proposalId123";
	}

	@Test
	public void findTask() {
		ProcessInstance pi = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
		ProcessDefinition pd = procTopoQuery.findProcessDefinition(pi);
		String processId = pd.getProcessDefinitionId();
		ProcessDefinition pd2 = procTopoQuery.findProcessDefinition(processId);
		Task task = procTopoQuery.findTask(pd, taskName);
		Task task2 = procTopoQuery.findTask(pd2, taskName);

		assertEquals(pd, pd2);
		assertEquals(task, task2);
		assertEquals(taskName, task2.getTaskName());
	}

	@Test
	public void hasRoleForTask() {
		ProcessInstance pi = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
		ProcessDefinition pd = procTopoQuery.findProcessDefinition(pi);
		String processId = pd.getProcessDefinitionId();
		boolean hasRoleForTask = ((TestUsherImpl) usher).hasRoleForTask(username, processId, taskName);
		assertTrue(hasRoleForTask);
	}

	@Test
	public void isActiveTask() {
		boolean isActiveTask = ((TestUsherImpl) usher).isActiveTask(taskName, appInstanceId);
		assertTrue(isActiveTask);
	}

	@Test
	public void canExecute() {
		boolean canExecute = usher.canExecute(username, taskName, appInstanceId);
		assertTrue(canExecute);
	}

	@Test
	public void execute() {
		executor.execute(appInstanceId, taskName);
		ProcessInstance processInstance = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
		StateMachineInstance stateMachineInstance = (StateMachineInstance) processInstance;
		StateMachineState activeState = stateMachineInstance.getActiveState();
		assertEquals(ExampleSmProcessStates.STATE2, activeState);
	}
}
