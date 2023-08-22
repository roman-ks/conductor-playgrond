package com.mashkario.conductor.sleeping;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class SleepingNicknameWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(SleepingNicknameWorker.class);

    private final String taskDefName;
    private final String workerId;

    public SleepingNicknameWorker(String taskDefName) {
        this.taskDefName = taskDefName;
        this.workerId = Integer.toHexString(hashCode());
        logger.info("Created worker {}", workerId);
    }

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }

    @Override
    public TaskResult execute(Task task) {
        String name = (String)task.getInputData().get("name");
        System.out.println("Got name "+name);
        logger.info("Worker {}: Got name {}", workerId, name);

        Object sleepTime = task.getInputData().get("sleepTime");
        if(sleepTime!=null)
            sleep(sleepTime);

        var nickname = name + new Random().nextInt();
        logger.info("Worker {}: Nickname: {}", workerId, nickname);

        TaskResult result = new TaskResult(task);
        result.setStatus(TaskResult.Status.COMPLETED);

        //Register the output of the task
        result.getOutputData().put("nickname", nickname);
        return result;
    }

    private static void sleep(Object sleepTime) {
        try {
            Thread.sleep(Integer.parseInt((String) sleepTime));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e){
            logger.warn("Not sleeping");
        }
    }
}