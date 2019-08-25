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
     * Username -> Bank Profiles
     * BankProfile -> Roles
     */
    Iterable<ProcessRole> findRoles(String username);
}