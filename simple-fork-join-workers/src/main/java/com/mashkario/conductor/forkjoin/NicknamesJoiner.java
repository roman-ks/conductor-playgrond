package com.mashkario.conductor.forkjoin;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
public class NicknamesJoiner implements Worker {

    @Override
    public String getTaskDefName() {
        return "join_nicknames";
    }

    @Override
    public TaskResult execute(Task task) {
        //todo why the map is wrapped in "names" ??
        Map<Integer, String> nicknamesMap = ((Map<String, Object>)task.getInputData().get("names"))
                .entrySet()
                .stream()
                .filter(e -> ReferenceNameUtils.isReferenceNameForTask(e.getKey(), NamesSplitter.NICKNAME_TASK))
                .collect(Collectors.toMap(
                        e -> ReferenceNameUtils.getReferenceOrder(e.getKey()),
                        e -> ((Map<String,String>) e.getValue()).get("nickname")
                ));

        Map<Integer, String> orderedNicknamesMap = new TreeMap<>(nicknamesMap);
        List<String> nicknamesList = List.copyOf(orderedNicknamesMap.values());

        log.info("Got {} nicknames", nicknamesMap.size());

        TaskResult result = new TaskResult(task);
        result.setStatus(TaskResult.Status.COMPLETED);
        result.getOutputData().put("nicknames", nicknamesList);

        return result;
    }
}
