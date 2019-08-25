package lucaster.poc.statemachine.exploration2;

interface Usher {
    boolean canExecute(String username, String taskName, String appInstanceId);
}

abstract class AbstractUsher implements Usher {

    private final UsherProcessIntegrationQuery procIntegrQuery;
    private final ProcessTopologyQuery procTopoQuery;
    private final UsherRoleQuery roleQuery;

    public AbstractUsher(   UsherProcessIntegrationQuery procIntegrQuery, 
                            ProcessTopologyQuery procTopoQuery,
                            UsherRoleQuery roleQuery) {
        this.procIntegrQuery = procIntegrQuery;
        this.procTopoQuery = procTopoQuery;
        this.roleQuery = roleQuery;
    }

    // Template method
    @Override
    final public boolean canExecute(String username, String taskName, String appInstanceId) {
        ProcessInstance pi = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
        ProcessDefinition pd = procTopoQuery.findProcessDefinition(pi);
        String processId = pd.getProcessDefinitionId();
        boolean isInstanceCompleted = pi.isCompleted();
        boolean hasRoleForTask = hasRoleForTask(username, processId, taskName);
		boolean isActiveTask = isActiveTask(taskName, appInstanceId);
		boolean isTheActivePersonForTaskOfInstance = isTheActivePersonForTaskOfInstance(username, taskName, appInstanceId);
		return !isInstanceCompleted && hasRoleForTask && isActiveTask && isTheActivePersonForTaskOfInstance;
    }

    // Process Definition concern
    final boolean hasRoleForTask(String username, String processId, String taskName) {
        Iterable<ProcessRole> userRoles = roleQuery.findRoles(username);
        ProcessDefinition pd = procTopoQuery.findProcessDefinition(processId);
        Task task = procTopoQuery.findTask(pd, taskName);
        Iterable<ProcessRole> taskRoles = procTopoQuery.getAllowedRolesOfTask(task);
        return Utils.anyEquals(userRoles, taskRoles);
    }

    // Process Instance concern
    final boolean isActiveTask(String taskName, String appInstanceId) {
        ProcessInstance pi = procIntegrQuery.findProcessInstanceByAppIntanceId(appInstanceId);
        ProcessDefinition pd = procTopoQuery.findProcessDefinition(pi);
        Task task = procTopoQuery.findTask(pd, taskName);
        Iterable<Task> activeTasks = procTopoQuery.getActiveTasks(pi);
        return Utils.anyEquals(task, activeTasks);
    }

    /**
     * This implementation is application-specific.<br />
     * SM does not know single users, so this becomes an application concern.<br />
     * BPMN might know for example the process instance owner user by means of process variables.
     */
    abstract boolean isTheActivePersonForTaskOfInstance(String username, String taskName, String appInstanceId);
}

interface UsherRoleQuery {
    /**
     * Username -> Bank Profiles
     * BankProfile -> Roles
     */
    Iterable<ProcessRole> findRoles(String username);
}

interface UsherProcessIntegrationQuery {
    ProcessInstance findProcessInstanceByAppIntanceId(String appInstanceId);
}
