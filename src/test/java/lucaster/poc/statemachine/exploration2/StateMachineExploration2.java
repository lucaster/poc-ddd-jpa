package lucaster.poc.statemachine.exploration2;

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

    private Query query;

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
        Iterable<Role> userRoles = query.findRoles(username);
        ProcessDefinition pd = query.findProcessDefinition(processId);
        Task task = query.findTask(pd, taskName);
        Iterable<Role> taskRoles = query.findTaskRoles(task);
        return anyEquals(userRoles, taskRoles);
    }

    // Process Instance concern
    boolean isActiveTask(String taskName, String appInstanceId) {
        ProcessInstance pi = query.findProcessInstance(appInstanceId);
        ProcessDefinition pd = query.findProcessDefinition(pi);
        Task task = query.findTask(pd, taskName);
        Iterable<Task> activeTasks = query.getActiveTasks(pi);
        return anyEquals(task, activeTasks);
    }

    /**
     * Application - specific
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

interface Query {
    Iterable<Role> findRoles(String username);
    ProcessDefinition findProcessDefinition(String processId);
    Task findTask(ProcessDefinition pd, String taskName);
    Iterable<Role> findTaskRoles(Task task);
    ProcessInstance findProcessInstance(String appInstanceId);
    ProcessDefinition findProcessDefinition(ProcessInstance pi);
    // If SM, active tasks are the current state's outgoing transitions. If BPMN, active tasks are the active tasks.
    Iterable<Task> getActiveTasks(ProcessInstance pi);
}

interface Role {}
interface ProcessDefinition {}
interface Task {}
interface ProcessInstance {}
