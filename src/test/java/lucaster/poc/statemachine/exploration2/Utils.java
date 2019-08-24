package lucaster.poc.statemachine.exploration2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
    static String makeFullyQualifiedStateName(String processDefinitionId, String stateName) {
    	 return String.format("%s|||%s", processDefinitionId, stateName);
    }
}