package com.cucc.vertx.demo.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Sender extends AbstractVerticle {
    
    @Override
    public void start(){
      EventBus eb = vertx.eventBus();
      // Send a message every second
      vertx.setPeriodic(1000, v -> {

        eb.send("ping-address", "ping!", reply -> {
          if (reply.succeeded()) {
            System.out.println("Received reply " + reply.result().body());
          } else {
            System.out.println("No reply");
          }
        });

      });
      
      
    }
  }