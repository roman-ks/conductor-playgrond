package com.mashkario.conductor.sleeping;

import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        TaskClient taskClient = new TaskClient();
        taskClient.setRootURI("http://localhost:8080/api/"); // Point this to the server API

        var workersCount = Integer.parseInt(System.getProperty("workers.count"));

        List<Worker> workers = IntStream.range(0, workersCount)
                .mapToObj(i -> new SleepingNicknameWorker("get_nickname_sleep"))
                .collect(Collectors.toList());

        // Create TaskRunnerConfigurer
        TaskRunnerConfigurer configurer =
                new TaskRunnerConfigurer.Builder(taskClient, workers)
                        .withThreadCount(workersCount)
                        .build();

        // Start the polling and execution of tasks
        configurer.init();
    }
}