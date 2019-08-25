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
	String taskName1;
	String taskName2;
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
		taskName1 = ExampleSmProcessTransitions.TASK1.getTaskName();
		taskName2 = ExampleSmProcessTransitions.TASK2.getTaskName();
		appInstanceId = "proposalId123";
	}

	@Test
	public void findTask() {
		ProcessInstance pi = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
		ProcessDefinition pd = procTopoQuery.findProcessDefinition(pi);
		String processId = pd.getProcessDefinitionId();
		ProcessDefinition pd2 = procTopoQuery.findProcessDefinition(processId);
		Task task = procTopoQuery.findTask(pd, taskName1);
		Task task2 = procTopoQuery.findTask(pd2, taskName1);

		assertEquals(pd, pd2);
		assertEquals(task, task2);
		assertEquals(taskName1, task2.getTaskName());
	}

	@Test
	public void hasRoleForTask() {
		ProcessInstance pi = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
		ProcessDefinition pd = procTopoQuery.findProcessDefinition(pi);
		String processId = pd.getProcessDefinitionId();
		boolean hasRoleForTask = ((TestUsherImpl) usher).hasRoleForTask(username1, processId, taskName1);
		assertTrue(hasRoleForTask);
	}

	@Test
	public void isActiveTask() {
		boolean isActiveTask = ((TestUsherImpl) usher).isActiveTask(taskName1, appInstanceId);
		assertTrue(isActiveTask);
	}

	@Test
	public void canExecute() {
		assertTrue(usher.canExecute(username1, taskName1, appInstanceId));
		assertFalse(usher.canExecute(username1, taskName2, appInstanceId));
		assertFalse(usher.canExecute(username2, taskName1, appInstanceId));
		assertFalse(usher.canExecute(username2, taskName2, appInstanceId));
	}

	@Test
	public void execute() {		
		executor.execute(username1, appInstanceId, taskName1);
		
		ProcessInstance processInstance = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
		StateMachineInstance stateMachineInstance = (StateMachineInstance) processInstance;
		StateMachineState activeState = stateMachineInstance.getActiveState();
		assertEquals(ExampleSmProcessStates.STATE2, activeState);
		
		executor.execute(username2, appInstanceId, taskName2);
		
		ProcessInstance processInstance2 = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
		StateMachineInstance stateMachineInstance2 = (StateMachineInstance) processInstance2;
		StateMachineState activeState2 = stateMachineInstance2.getActiveState();
		assertEquals(ExampleSmProcessStates.STATE3, activeState2);
		
		assertFalse(usher.canExecute(username1, taskName1, appInstanceId));
		assertFalse(usher.canExecute(username1, taskName2, appInstanceId));
		assertFalse(usher.canExecute(username2, taskName1, appInstanceId));
		assertFalse(usher.canExecute(username2, taskName2, appInstanceId));
	}
}
