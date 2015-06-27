package com.cucc.vertx.demo.main;


import com.cucc.vertx.demo.manger.VerticleManager;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class App {

    public static void main(String[] args) {
        
        DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject());
        Vertx.factory.vertx().deployVerticle(VerticleManager.class.getCanonicalName(), options,asyncResult->{
            if (asyncResult.succeeded()) {
                System.out.println("VerticleManager 部署完成： "+ asyncResult.result());
            } else {
                asyncResult.cause().printStackTrace();
            }
        });
        
    }

}
