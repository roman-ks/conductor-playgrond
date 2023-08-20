package com.mashkario.conductor.forkjoin;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NamesSplitter implements Worker {

    public static final String NICKNAME_TASK = "get_nickname";
    @Override
    public String getTaskDefName() {
        return "split_names";
    }

    @Override
    public TaskResult execute(Task task) {
        String names = (String) task.getInputData().get("names");
        log.info("Got names: {}", names);

        String[] individualNames = names.split(",");
        log.info("Names count: {}", individualNames.length);

        ArrayList<Map<String, Object>> dynamicTasks = new ArrayList<>();
        Map<String, Map<String, Object>> dynamicTasksInput = new HashMap<>();
        for (int i = 0; i < individualNames.length; i++) {
            String name = individualNames[i];
            String reference = ReferenceNameUtils.createReferenceName(NICKNAME_TASK, i);
            dynamicTasks.add(createDynamicTaskJson(NICKNAME_TASK, reference));

            dynamicTasksInput.put(reference, Map.of("name", name));
        }

        TaskResult result = new TaskResult(task);
        result.setStatus(TaskResult.Status.COMPLETED);
        result.getOutputData().put("dynamicForkTasksParam", dynamicTasks);
        result.getOutputData().put("dynamicForkTasksInputParamName", dynamicTasksInput);

        return result;
    }

    private static Map<String, Object> createDynamicTaskJson(String name, String reference) {
        return Map.of(
                "taskReferenceName", reference,
                "name", name,
                "type", "SIMPLE");
    }

}
