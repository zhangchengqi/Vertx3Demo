package com.cucc.vertx.demo.database;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;

public class MyVerticle extends AbstractVerticle {
    Vertx vertx = Vertx.vertx();
    
    public JsonObject config = new JsonObject()
            .put("url", "jdbc:oracle:thin:@10.124.0.39:1521:ngact")
            .put("driver_class", "oracle.jdbc.driver.OracleDriver")
            .put("user", "ubak").put("password", "ubak_123")
            .put("initial_pool_size", 5)
            .put("min_pool_size", 5)
            .put("max_pool_size", 30);

    public void start() {

        JDBCClient client = JDBCClient.createShared(vertx, config);
        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            public void handle(final HttpServerRequest httpServerRequest) {
                client.getConnection(res -> {
                    if (res.succeeded()) {

                        SQLConnection connection = res.result();

                        connection.query(
                                "SELECT USER FROM dual",
                                res2 -> {
                                    if (res2.succeeded()) {

                                        ResultSet rs = res2.result();
                                        httpServerRequest.response().end(
                                                "Hello world" + rs.getNumRows() + rs.getResults().toString());
                                        System.out.println(rs.getNumRows() + rs.getResults().toString());
                                        connection.close(res3->{
                                            if (res3.succeeded()) {
                                                System.out.println("close successful");
                                            }else {
                                                System.out.println("close failed");
                                            }
                                        });
                                        // Do something with results
                                    }
                                });
                    } else {
                        // Failed to get connection - deal with it
                    }
                    System.out.println(" I am here");
                });

                System.out.println(" I am here 222");
            }
        }).listen(8888, "localhost");
    }
}