package com.mashkario.conductor;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class NicknameWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(NicknameWorker.class);

    private final String taskDefName;

    public NicknameWorker(String taskDefName) {
        this.taskDefName = taskDefName;
    }

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }

    @Override
    public TaskResult execute(Task task) {
        String name = (String)task.getInputData().get("name");
        System.out.println("Got name "+name);
        logger.info("Got name {}", name);

        var nickname = name + new Random().nextInt();
        logger.info("Nickname: {}", nickname);

        TaskResult result = new TaskResult(task);
        result.setStatus(TaskResult.Status.COMPLETED);

        //Register the output of the task
        result.getOutputData().put("nickname", nickname);
        return result;
    }
}