package com.mashkario.conductor.desicion;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class CoolNicknameWorker implements Worker {

    private static final char[] COOL_CHARS = "_*~+-".toCharArray();
    private static final Random random = new Random();

    @Override
    public String getTaskDefName() {
        return "get_cool_nickname";
    }

    @Override
    public TaskResult execute(Task task) {
        String name = (String) task.getInputData().get("name");
        log.info("Got name: {}", name);

        int charsPairsToAdd = random.nextInt(8) + 3;
        String nickname = coolify(name, charsPairsToAdd);
        log.info("Nickname: {}", nickname);

        TaskResult result = new TaskResult(task);
        result.setStatus(TaskResult.Status.COMPLETED);
        result.getOutputData().put("nickname", nickname);
        return result;
    }

    private String coolify(String name, int pairsToAdd) {
        if (pairsToAdd <= 0)
            return name;

        int charNumber = random.nextInt(COOL_CHARS.length);
        var coolChar = COOL_CHARS[charNumber];
        var nickname = coolChar + name + coolChar;
        return coolify(nickname, pairsToAdd - 1);
    }


}
