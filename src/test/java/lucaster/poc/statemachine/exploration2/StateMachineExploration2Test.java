package lucaster.poc.statemachine.exploration2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class StateMachineExploration2Test {

	ProcessRoleRepository processRoleRepository;
	IntegrationRepository integrationRepository;
	ProcessDefinitionRepository processDefinitionRepository;
	UsherProcessIntegrationQuery procIntegrQuery;
	UsherProcessTopologyQuery procTopoQuery;
	UsherRoleQuery roleQuery;
	Usher usher;

	String username;
	String taskName;
	String appInstanceId;

	@Before
	public void setup() {
		processRoleRepository = new TestProcessRoleRepository();
		integrationRepository = new TestSimpleIntegrationRepository();
		processDefinitionRepository = new EnumDrivenProcessDefinitionRepository();
		procIntegrQuery = new UsherProcessIntegrationQueryImpl(integrationRepository);
		procTopoQuery = new ModelDrivenUsherProcessTopologyQuery(processDefinitionRepository);
		roleQuery = new UsherRoleQueryImpl(processRoleRepository);
		usher = new TestUsherImpl(procIntegrQuery, procTopoQuery, roleQuery);

		username = "EE53414";
		taskName = ExampleSmProcessTransitions.TASK1.getTaskName();
		appInstanceId = "proposalId123";
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
	public void explore() {

		boolean can = usher.canExecute(username, taskName, appInstanceId);
		assertTrue(can);
	}
}
