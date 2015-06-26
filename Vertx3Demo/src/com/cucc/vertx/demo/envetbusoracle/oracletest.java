package com.cucc.vertx.demo.envetbusoracle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

public class oracletest extends AbstractVerticle{
    public JsonObject config = new JsonObject()
  .put("url", "jdbc:oracle:thin:@10.124.0.42:1521:tact1")
//  .put("url", "jdbc:oracle:thin:@192.168.160.100:1521:orcl")
  .put("driver_class", "oracle.jdbc.driver.OracleDriver")
//  .put("user", "uop_act1").put("password", "uop_act1")
   .put("user", "ucr_act1").put("password", "ucr_act1")
  .put("initial_pool_size", 5)
  .put("min_pool_size", 5)
  .put("max_pool_size", 30);
    public JDBCClient client;
    @Override
    public void start(){
        EventBus eb = vertx.eventBus();
        //JDBCClient client2=vertx.sharedData().getLocalMap("");
        JDBCClient  client = JDBCClient.createShared(vertx, config,"act1");
        
        System.out.println("2222222222222");
        MessageConsumer<JsonObject> consumer4 = eb.consumer("test");
        consumer4.handler(message->{

            doSelect(message);
            
        });
    }
    
    private void doSelect( Message<JsonObject> message ) {
        System.out.println("srart doselect"+message.body().getString("msisdn"));
        
        client.getConnection(res -> {
        if (res.succeeded()) {
        SQLConnection connection = res.result();
        
        //String stmt =  "select user_id from ucr_act1.tf_f_user where net_type_code = '50'  and serial_number =?";
        String stmt =  "select user from dual";
        connection.query(stmt,resultSet -> {
                 if (resultSet.succeeded()) {
                     System.out.println("excute query");
                     ResultSet rs = resultSet.result();
                     message.reply(rs.toJson());
                    }else {
                        System.out.println("excute failed1"+resultSet.cause());
                    }
                    connection.close(res3->{
                     if (res3.succeeded()) {
                         System.out.println("close successful");
                    }else {
                        System.out.println("close fail"+res3.cause());
                    }
                 });
                });
            } else {
                System.out.println("excute failed2");
            }
            System.out.println(" I am here");
        });
      }
    
}
