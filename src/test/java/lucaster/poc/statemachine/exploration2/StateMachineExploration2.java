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

    private UsherProcessQuery procQuery;
    private UsherRoleQuery roleQuery;

    public boolean canExecute(String username, String processId, String taskName, String appInstanceId) {
        return 
            hasRoleForTask(username, processId, taskName) 
            && 
            isActiveTask(taskName, appInstanceId) 
            && 
            isTheActivePersonForTaskOfInstance(username, taskName, appInstanceId)
            ;
    }

    // Process Definition concern
    boolean hasRoleForTask(String username, String processId, String taskName) {
        Iterable<Role> userRoles = roleQuery.findRoles(username);
        ProcessDefinition pd = procQuery.findProcessDefinition(processId);
        Task task = procQuery.findTask(pd, taskName);
        Iterable<Role> taskRoles = procQuery.findTaskRoles(task);
        return anyEquals(userRoles, taskRoles);
    }

    // Process Instance concern
    boolean isActiveTask(String taskName, String appInstanceId) {
        ProcessInstance pi = procQuery.findProcessInstance(appInstanceId);
        ProcessDefinition pd = procQuery.findProcessDefinition(pi);
        Task task = procQuery.findTask(pd, taskName);
        Iterable<Task> activeTasks = procQuery.getActiveTasks(pi);
        return anyEquals(task, activeTasks);
    }

    /**
     * SM does not know single users, so this becomes an application concern
     * BPMN might know for example the process instance owner user by means of process variables.
     */
    abstract boolean isTheActivePersonForTaskOfInstance(String username, String taskName, String appInstanceId);

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

interface UsherRoleQuery {
    Iterable<Role> findRoles(String username);
}

// A repository of Process topology objects
interface UsherProcessTopologyQuery {
    ProcessDefinition findProcessDefinition(String processId);
    Task findTask(ProcessDefinition pd, String taskName);
    Iterable<Role> findTaskRoles(Task task);
    ProcessDefinition findProcessDefinition(ProcessInstance pi);
    // If SM, active tasks are the current state's outgoing transitions. If BPMN, active tasks are the active tasks.
    Iterable<Task> getActiveTasks(ProcessInstance pi);
}

interface UsherProcessQuery extends UsherProcessTopologyQuery {
    ProcessInstance findProcessInstance(String appInstanceId);
}

abstract class EnumStateMachineUsherProcessTopologyQuery implements UsherProcessTopologyQuery {
    @Override
    public ProcessDefinition findProcessDefinition(String processId) {
        return ProcessDefinitions.valueOf(processId);
    }
    @Override
    public Task findTask(ProcessDefinition pd, String taskName) {
        return pd.findTaskByName(taskName);
    }
}

interface Role {}
interface ProcessDefinition {
    String getProcessId();
    Task findTaskByName(String taskName);
}
interface Task {
    String getTaskName();
}
interface ProcessInstance {}

enum ProcessDefinitions implements ProcessDefinition {
    EXAMPLE_SM_PROCESS {
        @Override
        public String getProcessId() {
            return "EXAMPLE_SM_PROCESS";
        }
        @Override
        protected Iterable<Task> getTasks() {
            Set<Task> tasks = new HashSet<>(); // TODO make this a field populated in ctor
            tasks.add(ExampleSmProcessTasks.TASK1);
            tasks.add(ExampleSmProcessTasks.TASK2);
            return tasks;
        }
    };
    public abstract String getProcessId();
    protected abstract Iterable<Task> getTasks();
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
    TASK1 {
        @Override
        public String getTaskName() {
            return "TASK1";
        }
    }, 
    TASK2 {
        @Override
        public String getTaskName() {
            return "TASK1";
        }
    };
}