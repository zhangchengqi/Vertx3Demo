package com.cucc.vertx.demo.database;

import com.cucc.vertx.demo.database.base.OracleClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;


public class testclinet extends AbstractVerticle {

    public void start() {

        JDBCClient client = OracleClient.getInstance(vertx).getClient("");
        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            public void handle(final HttpServerRequest httpServerRequest) {
                client.getConnection(res -> {
                if (res.succeeded()) {
                SQLConnection connection = res.result();
                connection.query("SELECT USER FROM dual", res2 -> {
                         if (res2.succeeded()) {
                             ResultSet rs = res2.result();
                             System.out.println(rs);
                             httpServerRequest.response().end(
                                                "Hello world" + rs.getNumRows() + rs.getResults().toString());
                             System.out.println(rs.getNumRows() + rs.getResults().toString());

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