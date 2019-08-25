package lucaster.poc.statemachine.exploration2;

class EnumDrivenProcessDefinitionRepository implements ProcessDefinitionRepository {
	@Override public ProcessDefinition findProcessDefinition(String processId) {
		return StateMachineProcessDefinition.valueOf(processId);
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

class UsherProcessTopologyQueryImpl implements ProcessTopologyQuery {
	private final ProcessDefinitionRepository processDefinitionRepository;
	UsherProcessTopologyQueryImpl(ProcessDefinitionRepository processDefinitionRepository) {
		this.processDefinitionRepository = processDefinitionRepository;
	}
    @Override
    public ProcessDefinition findProcessDefinition(String processDefinitionId) {
    	return processDefinitionRepository.findProcessDefinition(processDefinitionId);
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

class UsherProcessIntegrationQueryImpl implements UsherProcessIntegrationQuery {
    private final ProcessIntegrationRepository integrationRepo;
    public UsherProcessIntegrationQueryImpl(ProcessIntegrationRepository integrationRepo) {
		this.integrationRepo = integrationRepo;
	}
    @Override public ProcessInstance findProcessInstanceByAppIntanceId(String appInstanceId) {
        return integrationRepo.findProcessInstanceByAppInstanceId(appInstanceId);
    }
}