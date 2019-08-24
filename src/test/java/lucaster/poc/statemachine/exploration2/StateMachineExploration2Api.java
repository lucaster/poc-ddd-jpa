package lucaster.poc.statemachine.exploration2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

interface Usher {
    boolean canExecute(String username, String taskName, String appInstanceId);
}

abstract class AbstractUsher implements Usher {

    private final UsherProcessIntegrationQuery procIntegrQuery;
    private final UsherProcessTopologyQuery procTopoQuery;
    private final UsherRoleQuery roleQuery;

    public AbstractUsher(   UsherProcessIntegrationQuery procIntegrQuery, 
                            UsherProcessTopologyQuery procTopoQuery,
                            UsherRoleQuery roleQuery) {
        this.procIntegrQuery = procIntegrQuery;
        this.procTopoQuery = procTopoQuery;
        this.roleQuery = roleQuery;
    }

    // Template method
    @Override
    final public boolean canExecute(String username, String taskName, String appInstanceId) {
        ProcessInstance pi = procIntegrQuery.findProcessInstance(appInstanceId);
        ProcessDefinition pd = procTopoQuery.findProcessDefinition(pi);
        String processId = pd.getProcessId();
        boolean hasRoleForTask = hasRoleForTask(username, processId, taskName);
		boolean isActiveTask = isActiveTask(taskName, appInstanceId);
		boolean isTheActivePersonForTaskOfInstance = isTheActivePersonForTaskOfInstance(username, taskName, appInstanceId);
		return hasRoleForTask && isActiveTask && isTheActivePersonForTaskOfInstance;
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
        ProcessInstance pi = procIntegrQuery.findProcessInstance(appInstanceId);
        ProcessDefinition pd = procTopoQuery.findProcessDefinition(pi);
        Task task = procTopoQuery.findTask(pd, taskName);
        Iterable<Task> activeTasks = procTopoQuery.getActiveTasks(pi);
        return Utils.anyEquals(task, activeTasks);
    }

    /**
     * SM does not know single users, so this becomes an application concern.
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

// A repository of Process topology objects
interface UsherProcessTopologyQuery {
    ProcessDefinition findProcessDefinition(String processId);
    Task findTask(ProcessDefinition pd, String taskName);
    Iterable<ProcessRole> getAllowedRolesOfTask(Task task);
    ProcessDefinition findProcessDefinition(ProcessInstance pi);
    // If SM, active tasks are the current state's outgoing transitions. If BPMN, active tasks are the active tasks.
    Iterable<Task> getActiveTasks(ProcessInstance pi);
}

interface UsherProcessIntegrationQuery {
    ProcessInstance findProcessInstance(String appInstanceId);
}

final class ModelDrivenUsherProcessTopologyQuery implements UsherProcessTopologyQuery {
    @Override
    public ProcessDefinition findProcessDefinition(String processId) {
        return StateMachineDefinitions.valueOf(processId);
    }
    @Override
    public Task findTask(ProcessDefinition pd, String taskName) {
        return pd.findTaskByName(taskName);
    }
    @Override
    public Iterable<ProcessRole> getAllowedRolesOfTask(Task task) {
        return task.getAllowedRoles();
    }
    @Override
    public ProcessDefinition findProcessDefinition(ProcessInstance pi) {
        return pi.getProcessDefinition();
    }
    @Override
    public Iterable<Task> getActiveTasks(ProcessInstance pi) {
        return pi.getActiveTasks();
    }
}





abstract class Utils {
    private Utils() {}
    @SafeVarargs
    public static <T> Set<T> toSet(T... args) {
        Set<T> set = new HashSet<>();
        for (T t : args) {
            set.add(t);
        }
        return Collections.unmodifiableSet(set);
    }
    static <T> boolean anyEquals(Iterable<T> roles1, Iterable<T> roles2) {
        for (T role1 : roles1) {
            if (anyEquals(role1, roles2)){
                return true;
            } 
        }
        return false;
    }
    static <T> boolean anyEquals(T task1, Iterable<T> tasks2) {
        for (T activeTask : tasks2) {
            if (activeTask.equals(task1)) {
                return true;
            }
        }
        return false;
    }
}