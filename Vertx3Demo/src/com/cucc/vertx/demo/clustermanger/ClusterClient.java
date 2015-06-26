package com.cucc.vertx.demo.clustermanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class ClusterClient extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(ClusterClient.class);

    @Override
    public void start() throws Exception {
        System.out.println("start cluster client");
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer("message", message -> {
            logger.info("recv " + message.body());
            message.reply("hello this from client");
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}

