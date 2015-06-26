package com.cucc.vertx.demo.database;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;


public class oraclebus extends AbstractVerticle implements Handler<Message<JsonObject>> {
    private static final Logger logger =LoggerFactory.getLogger(oraclebus.class);
    private oraclebus that=this;
    public JsonObject config = new JsonObject()
    .put("url", "jdbc:oracle:thin:@10.124.0.39:1521:ngact")
    .put("driver_class", "oracle.jdbc.driver.OracleDriver")
    .put("user", "ubak").put("password", "ubak_123")
    .put("initial_pool_size", 5)
    .put("min_pool_size", 5)
    .put("max_pool_size", 30); 
    private JDBCClient client;
    @Override
    public void start(){
        client = JDBCClient.createShared(vertx, config);
        EventBus eb = vertx.eventBus();
        
        MessageConsumer<String> consumer = eb.consumer("ping-address");
        consumer.handler(message->{
            System.out.println(message.body());
            
        }
        );

    }

    @Override
    public void handle(final Message<JsonObject> message) {
        // TODO Auto-generated method stub
        String action = message.body().getString( "action" ) ;

        if( action == null ) {
         // sendError( message, "action must be specified" ) ;
        }
        switch( action ) {
          case "select" :
            doSelect( message ) ;
            break ;
        }

      }
    
    private void doSelect( Message<JsonObject> message ) {
        client.getConnection(res -> {
        if (res.succeeded()) {
        SQLConnection connection = res.result();
        connection.query("SELECT USER FROM dual", res2 -> {
                 if (res2.succeeded()) {
                     ResultSet rs = res2.result();
                     System.out.println(rs);
                     message.reply("Hello world" + rs.getNumRows() + rs.getResults().toString());
                     System.out.println(rs.getNumRows() + rs.getResults().toString());
                    }
                });
            } else {
                // Failed to get connection - deal with it
            }
            System.out.println(" I am here");
        });
      }
    
    
    

}
