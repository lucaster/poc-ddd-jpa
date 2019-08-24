package lucaster.poc.statemachine.exploration2;

/**
 * Links Process Instance and app-specific data
 * @author Luca Cavagnoli
 *
 */
class AppProcInst {
	// PROCESS_INSTANCE_ID
    final ProcessInstance pi;
    // APP_INSTANCE_ID
    final String appInstanceId;
    protected Long versionForOptimisticLocking;
    AppProcInst(ProcessInstance pi, String appInstanceId) {
        this.pi = pi;
        this.appInstanceId = appInstanceId;
    }
}
