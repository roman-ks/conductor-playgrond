package com.mashkario.conductor.runner;

import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Builder
public class ConductorStarter {

    private List<Worker> workers;
    private int threadCount;
    @Builder.Default
    private String rootUri = "http://localhost:8080/api/";

    public void start() {
        log.info("Staring with rootUri {}, workers: {}, threads: {}",
                this.rootUri, this.workers.size(), this.threadCount);

        TaskClient taskClient = new TaskClient();
        taskClient.setRootURI(this.rootUri); // Point this to the server API

        // Create TaskRunnerConfigurer
        TaskRunnerConfigurer configurer =
                new TaskRunnerConfigurer.Builder(taskClient, this.workers)
                        .withThreadCount(this.threadCount)
                        .build();

        // Start the polling and execution of tasks
        configurer.init();
    }

}
