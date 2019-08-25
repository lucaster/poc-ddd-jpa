package lucaster.poc.statemachine.exploration2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

	String username1;
	String username2;
	String task1Name;
	String task2Name;
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
		executor = new ExecutorImpl(executorQuery, integrationRepository, usher);

		username1 = "EE53414";
		username2 = "EE37987";
		task1Name = ExampleSmTransitions.TASK1.getTaskName();
		task2Name = ExampleSmTransitions.TASK2.getTaskName();
		appInstanceId = "proposalId123";
	}

	@Test
	public void findTask() {
		ProcessInstance pi = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
		ProcessDefinition pd = procTopoQuery.findProcessDefinition(pi);
		String processId = pd.getProcessDefinitionId();
		ProcessDefinition pd2 = procTopoQuery.findProcessDefinition(processId);
		Task task = procTopoQuery.findTask(pd, task1Name);
		Task task2 = procTopoQuery.findTask(pd2, task1Name);

		assertEquals(pd, pd2);
		assertEquals(task, task2);
		assertEquals(task1Name, task2.getTaskName());
	}

	@Test
	public void hasRoleForTask() {
		ProcessInstance pi = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
		ProcessDefinition pd = procTopoQuery.findProcessDefinition(pi);
		String processId = pd.getProcessDefinitionId();
		boolean hasRoleForTask = ((TestUsherImpl) usher).hasRoleForTask(username1, processId, task1Name);
		assertTrue(hasRoleForTask);
	}

	@Test
	public void isActiveTask() {
		boolean isActiveTask = ((TestUsherImpl) usher).isActiveTask(task1Name, appInstanceId);
		assertTrue(isActiveTask);
	}

	@Test
	public void canExecute() {
		assertTrue(usher.canExecute(username1, task1Name, appInstanceId));
		assertFalse(usher.canExecute(username1, task2Name, appInstanceId));
		assertFalse(usher.canExecute(username2, task1Name, appInstanceId));
		assertFalse(usher.canExecute(username2, task2Name, appInstanceId));
	}

	@Test
	public void execute() {		
		executor.execute(username1, appInstanceId, task1Name);
		
		ProcessInstance processInstance = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
		StateMachineInstance stateMachineInstance = (StateMachineInstance) processInstance;
		StateMachineState activeState = stateMachineInstance.getActiveState();
		assertEquals(ExampleSmStates.STATE2, activeState);
		
		executor.execute(username2, appInstanceId, task2Name);
		
		ProcessInstance processInstance2 = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
		StateMachineInstance stateMachineInstance2 = (StateMachineInstance) processInstance2;
		StateMachineState activeState2 = stateMachineInstance2.getActiveState();
		assertEquals(ExampleSmStates.STATE3, activeState2);
		
		assertFalse(usher.canExecute(username1, task1Name, appInstanceId));
		assertFalse(usher.canExecute(username1, task2Name, appInstanceId));
		assertFalse(usher.canExecute(username2, task1Name, appInstanceId));
		assertFalse(usher.canExecute(username2, task2Name, appInstanceId));
	}

	@Test(expected = RuntimeException.class)
	public void cannotExecute() {
		executor.execute(username2, appInstanceId, task1Name);
	}
}
