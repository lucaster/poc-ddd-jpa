package lucaster.poc.statemachine.exploration2;

import java.util.HashSet;
import java.util.Set;

public class StateMachineExploration2 {
    void explore() {
        Usher usher = null;
        boolean can = usher.canExecute("P123456", "approveProposal", "proposal-id-123-456-789");
    }
}

interface Usher {
    boolean canExecute(String username, String taskId, String appInstanceId);
}

abstract class AbstractUsher implements Usher {

    private UsherProcessIntegrationQuery procIntegrQuery;
    private UsherProcessTopologyQuery procTopoQuery;
    private UsherRoleQuery roleQuery;

    // Template method
    final public boolean canExecute(String username, String processId, String taskName, String appInstanceId) {
        return 
            hasRoleForTask(username, processId, taskName) 
            && 
            isActiveTask(taskName, appInstanceId) 
            && 
            isTheActivePersonForTaskOfInstance(username, taskName, appInstanceId)
            ;
    }

    // Process Definition concern
    final boolean hasRoleForTask(String username, String processId, String taskName) {
        Iterable<Role> userRoles = roleQuery.findRoles(username);
        ProcessDefinition pd = procTopoQuery.findProcessDefinition(processId);
        Task task = procTopoQuery.findTask(pd, taskName);
        Iterable<Role> taskRoles = procTopoQuery.getAllowedRolesOfTask(task);
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
     * SM does not know single users, so this becomes an application concern
     * BPMN might know for example the process instance owner user by means of process variables.
     */
    abstract boolean isTheActivePersonForTaskOfInstance(String username, String taskName, String appInstanceId);
}

interface UsherRoleQuery {
    Iterable<Role> findRoles(String username);
}

// A repository of Process topology objects
interface UsherProcessTopologyQuery {
    ProcessDefinition findProcessDefinition(String processId);
    Task findTask(ProcessDefinition pd, String taskName);
    Iterable<Role> getAllowedRolesOfTask(Task task);
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
        return ProcessDefinitions.valueOf(processId);
    }
    @Override
    public Task findTask(ProcessDefinition pd, String taskName) {
        return pd.findTaskByName(taskName);
    }
    @Override
    public Iterable<Role> getAllowedRolesOfTask(Task task) {
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

interface ProcessDefinition {
    String getProcessId();
    Task findTaskByName(String taskName);
}
interface Task {
    String getTaskName();
    Iterable<Role> getAllowedRoles();
}
interface Role {}
interface ProcessInstance {
    ProcessDefinition getProcessDefinition();
    Iterable<Task> getActiveTasks();
}

enum ProcessDefinitions implements ProcessDefinition {

    EXAMPLE_SM_PROCESS(Utils.<Task>toSet(ExampleSmProcessTasks.TASK1, ExampleSmProcessTasks.TASK2)) {
        @Override
        public String getProcessId() {
            return "EXAMPLE_SM_PROCESS";
        }
    };

    protected final Iterable<Task> tasks;

    ProcessDefinitions(Iterable<Task> tasks) {
        this.tasks = tasks;
    }

    public abstract String getProcessId();

    protected Iterable<Task> getTasks() {
        return tasks;
    }

    public Task findTaskByName(String taskName) {
        for (Task t : getTasks()) {
            if (t.getTaskName().equals(taskName)) {
                return t;
            }
        }
        return null;
    }
}
enum ExampleSmProcessTasks implements Task {
    TASK1(Utils.<Role>toSet(ExampleSmProcessRoles.ROLE1)) {
        @Override
        public String getTaskName() {
            return "TASK1";
        }
    }, 
    TASK2(Utils.<Role>toSet(ExampleSmProcessRoles.ROLE2)) {
        @Override
        public String getTaskName() {
            return "TASK1";
        }
    };
    protected Iterable<Role> roles;
    ExampleSmProcessTasks(Iterable<Role> roles) {
        this.roles = roles;
    }
    @Override
    public Iterable<Role> getAllowedRoles() {
        return roles;
    }
}
enum ExampleSmProcessRoles implements Role {
    ROLE1,
    ROLE2;
}

abstract class Utils {
    private Utils() {}
    @SafeVarargs
    public static <T> Set<T> toSet(T... args) {
        Set<T> set = new HashSet<>();
        for (T t : args) {
            set.add(t);
        }
        return set;
    }
    static <T> boolean anyEquals(Iterable<T> roles1, Iterable<T> roles2) {
        for (T role1 : roles1) {
            if (anyEquals(role1, roles2)){
                return true;
            } 
        }
        return false;
    }
    static <T> boolean anyEquals(T task, Iterable<T> activeTasks) {
        for (T activeTask : activeTasks) {
            if (activeTask.equals(task)) {
                return true;
            }
        }
        return false;
    }
}