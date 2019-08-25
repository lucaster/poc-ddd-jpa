package lucaster.poc.statemachine.exploration2;

class EnumDrivenProcessDefinitionRepository implements ProcessDefinitionRepository {
	@Override public ProcessDefinition findProcessDefinition(String processId) {
		return StateMachineDefinitions.valueOf(processId);
	}
}

class UsherProcessIntegrationQueryImpl implements UsherProcessIntegrationQuery {
    private final IntegrationRepository integrationRepo;
    public UsherProcessIntegrationQueryImpl(IntegrationRepository integrationRepo) {
		this.integrationRepo = integrationRepo;
	}
    @Override public ProcessInstance findProcessInstanceByAppIntanceId(String appInstanceId) {
        return integrationRepo.findProcessInstanceByAppIntanceId(appInstanceId);
    }
}

class UsherRoleQueryImpl implements UsherRoleQuery {
	private final ProcessRoleRepository roleRepo;
	UsherRoleQueryImpl(ProcessRoleRepository roleRepo) {
		this.roleRepo = roleRepo;
	}
    @Override public Iterable<ProcessRole> findRoles(String username) {
    	return roleRepo.findRoles(username);
    }
}
