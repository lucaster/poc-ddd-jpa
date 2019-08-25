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
