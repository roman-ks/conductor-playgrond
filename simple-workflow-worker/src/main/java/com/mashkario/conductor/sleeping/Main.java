package com.mashkario.conductor.sleeping;

import com.mashkario.conductor.runner.ConductorStarter;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        List<Worker> workers = List.of(
                new SleepingNicknameWorker("get_nickname_sleep"));

        ConductorStarter.builder()
                .threadCount(1)
                .workers(workers)
                .build().start();
    }
}