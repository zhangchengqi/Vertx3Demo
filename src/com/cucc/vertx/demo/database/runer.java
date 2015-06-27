package com.cucc.vertx.demo.database;

import io.vertx.core.Vertx;




public class runer {
    public static void main(String[] args)  {
        Vertx vertx=Vertx.vertx();
        vertx.deployVerticle("com.cucc.vertx.demo.database.SimpleREST");
        
    }
}
