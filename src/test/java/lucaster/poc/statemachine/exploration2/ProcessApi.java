package lucaster.poc.statemachine.exploration2;

interface ProcessDefinition {
	String getProcessDefinitionId();
	Task findTaskByName(String taskName);
}
interface Task {
	String getTaskName();
	Iterable<ProcessRole> getAllowedRoles();
}
interface ProcessRole {
}
interface ProcessInstance {
	String getProcessInstanceId();
	ProcessDefinition getProcessDefinition();
	Iterable<Task> getActiveTasks();
	void executeTask(Task task);
	boolean isCompleted();
}
interface ProcessDefinitionRepository {
	ProcessDefinition findProcessDefinition(String processId);
}
interface ProcessRoleRepository {
    /**
	 * e.g.: Username -> Bank Profiles. Bank Profile -> Roles.
	 */
    Iterable<ProcessRole> findRoles(String username);
}
interface ProcessTopologyQuery {
	ProcessDefinition findProcessDefinition(String processDefinitionId);
	Task findTask(ProcessDefinition pd, String taskName);
	Iterable<ProcessRole> getAllowedRolesOfTask(Task task);
	ProcessDefinition findProcessDefinition(ProcessInstance pi);
	// If SM, active tasks are the current state's outgoing transitions. If BPMN, active tasks are the active tasks.
	Iterable<Task> getActiveTasks(ProcessInstance pi);
}