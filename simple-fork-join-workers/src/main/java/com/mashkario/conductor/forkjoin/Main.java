package com.mashkario.conductor.forkjoin;

import com.mashkario.conductor.runner.ConductorStarter;
import com.netflix.conductor.client.worker.Worker;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Worker> workers = List.of(
                new NamesSplitter(),
                new NicknamesJoiner());

        ConductorStarter.builder()
                .threadCount(2)
                .workers(workers)
                .build().start();
    }

}
