package lucaster.poc.statemachine.exploration2;

interface ProcessDefinition {
	String getProcessId();
	Task findTaskByName(String taskName);
}

interface Task {
	String getTaskName();
	Iterable<ProcessRole> getAllowedRoles();
}

interface ProcessRole {
}

interface ProcessInstance {
	ProcessDefinition getProcessDefinition();
	Iterable<Task> getActiveTasks();
}
