package com.cucc.vertx.demo.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

public class Receiver extends AbstractVerticle {

    @Override
    public void start(){

      EventBus eb = vertx.eventBus();

      eb.consumer("ping-address", message -> {

        System.out.println("Received message: " + message.body());
        // Now send back reply
        message.reply("pong!");
      });

      System.out.println("Receiver ready!");
    }
  }