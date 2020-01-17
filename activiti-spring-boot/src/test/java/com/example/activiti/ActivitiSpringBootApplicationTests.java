package com.example.activiti;

import com.example.activiti.config.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.model.payloads.ClaimTaskPayload;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ActivitiSpringBootApplicationTests {

	@Autowired
	private ProcessRuntime processRuntime;

	@Autowired
	private TaskRuntime taskRuntime;

	@Autowired
	private SecurityUtil securityUtil;

	/**
	 * 1.查看流程部署信息
	 * activiti7与SpringBoot整合之后会自动部署resources在的processes文件夹下的bpmn文件
	 */
	@Test
	public void findProcessInfo() {
		//指定用户
		securityUtil.logInAs("salaboy");
		Page<ProcessDefinition> page = processRuntime.processDefinitions(Pageable.of(0, 10));
		if (page.getTotalItems() > 0) {
			page.getContent().forEach(processDefinition -> {
				System.out.println("id: " + processDefinition.getId());
				System.out.println("key: " + processDefinition.getKey());
			});
		}
	}

	/**
	 * 2.启动流程实例
	 */
	@Test
	public void startProcessInstance() {
		securityUtil.logInAs("salaboy");
		ProcessInstance boot = processRuntime.start(ProcessPayloadBuilder.start().withProcessDefinitionKey("boot").withName("boot实例").withBusinessKey("10001").build());
		System.out.println("name: " + boot.getName());
		System.out.println("id: " + boot.getId());
	}

	/**
	 * 3.任务分页查询
	 */
	@Test
	public void queryPage() {
		securityUtil.logInAs("ryandawsonuk");
		Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
		if (tasks.getTotalItems() > 0) {
			System.out.println("任务数量: " + tasks.getTotalItems());
			tasks.getContent().forEach(task -> {
				System.out.println("执行者: " + task.getAssignee());
				System.out.println("流程定义id: " + task.getProcessDefinitionId());
				System.out.println("流程实例id: " + task.getProcessInstanceId());
				System.out.println("任务名称: " + task.getName());
				System.out.println(task.getStatus());
			});
		}
	}

	/**
	 * 4.任务拾取
	 */
	@Test
	public void claimTask() {
		securityUtil.logInAs("ryandawsonuk");
		Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
		if (tasks.getTotalItems() > 0) {
			System.out.println("任务数量: " + tasks.getTotalItems());
			tasks.getContent().forEach(task -> {
				Task claim = taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
				System.out.println("执行者: " + claim.getAssignee());
				System.out.println("流程定义id: " + claim.getProcessDefinitionId());
				System.out.println("流程实例id: " + claim.getProcessInstanceId());
				System.out.println("任务名称: " + claim.getName());
				System.out.println(claim.getStatus());
			});
		}
	}

	/**
	 * 5.任务完成
	 */
	@Test
	public void completeTask() {
		securityUtil.logInAs("ryandawsonuk");
		Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
		if (tasks.getTotalItems() > 0) {
			System.out.println("任务数量: " + tasks.getTotalItems());
			tasks.getContent().forEach(task -> {
				Task complete = taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
				System.out.println("执行者: " + complete.getAssignee());
				System.out.println("流程定义id: " + complete.getProcessDefinitionId());
				System.out.println("流程实例id: " + complete.getProcessInstanceId());
				System.out.println("任务名称: " + complete.getName());
				System.out.println(complete.getStatus());
			});
		}
	}
}
