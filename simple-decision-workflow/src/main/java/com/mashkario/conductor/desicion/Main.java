package com.mashkario.conductor.desicion;

import com.mashkario.conductor.NicknameWorker;
import com.mashkario.conductor.runner.ConductorStarter;
import com.netflix.conductor.client.worker.Worker;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Worker> workers = List.of(new CoolNicknameWorker(),
                new NicknameWorker("get_nickname"));

        ConductorStarter.builder()
                .threadCount(2)
                .workers(workers)
                .build().start();
    }
}
