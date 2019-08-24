package lucaster.poc.statemachine.exploration2;

/**
 * Links Process Instance and app-specific data
 * @author Luca Cavagnoli
 *
 */
class AppProcInst {
    final ProcessInstance pi;
    final String appInstanceId;
    protected Long versionForOptimisticLocking;
    AppProcInst(ProcessInstance pi, String appInstanceId) {
        this.pi = pi;
        this.appInstanceId = appInstanceId;
    }
}
