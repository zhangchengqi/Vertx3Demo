package com.cucc.vertx.demo.clustermanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class ClusterServer extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(ClusterServer.class);

    @Override
    public void start() throws Exception {
        logger.info("start cluster server");
        EventBus eventBus = vertx.eventBus();
        vertx.setPeriodic(5000, timer -> {
            logger.info("send message");
            eventBus.send("message", "Yay! Someone kicked a ball", ar -> {
                logger.info("send result  = " + ar.succeeded()+ar.result());
            });
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}