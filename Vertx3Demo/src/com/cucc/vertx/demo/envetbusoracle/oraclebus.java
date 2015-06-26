package com.cucc.vertx.demo.envetbusoracle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cucc.vertx.demo.object.User;
import com.cucc.vertx.demo.object.Userinfo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

public class oraclebus extends AbstractVerticle {
    
    private static Logger logger = LoggerFactory.getLogger(oraclebus.class);
//    public JsonObject config = new JsonObject()
//    .put("url", "jdbc:oracle:thin:@10.124.0.39:1521:ngact")
//    .put("driver_class", "oracle.jdbc.driver.OracleDriver")
//    .put("user", "ubak").put("password", "ubak_123")
//    .put("initial_pool_size", 5)
//    .put("min_pool_size", 5)
//    .put("max_pool_size", 30); 
    
    public JsonObject config = new JsonObject()
    .put("url", "jdbc:oracle:thin:@10.124.0.42:1521:tact1")
//    .put("url", "jdbc:oracle:thin:@192.168.160.100:1521:orcl")
    .put("driver_class", "oracle.jdbc.driver.OracleDriver")
    .put("user", "uop_act1").put("password", "uop_act1")
//     .put("user", "ucr_act1").put("password", "ucr_act1")
    .put("initial_pool_size", 5)
    .put("min_pool_size", 5)
    .put("max_pool_size", 30);
    
    public JDBCClient client;
    @Override
    public void start(){
        logger.info("start work");
        client = JDBCClient.createShared(vertx, config);
        EventBus eb = vertx.eventBus();
//        MessageConsumer<String> consumer = eb.consumer("ping-address");
//        consumer.handler(message->{
//            System.out.println(message.body());
//            
//        });
        MessageConsumer<JsonObject> consumer2 = eb.consumer("ping-address2");
        consumer2.handler(message->{
            doSelect(message);
            
        });
        
        
        MessageConsumer<JsonObject> consumer3 = eb.consumer("insert-number");
        consumer3.handler(message->{
            logger.info("start handle message");
            doinsertnum(message);
            
        });
        
        MessageConsumer<JsonObject> consumer4 = eb.consumer("insert");
        consumer4.handler(message->{
            logger.info("start handle message");
            doinsert(message);
            
        });
        
        MessageConsumer<User> consumer5 = eb.consumer("test");
        consumer5.handler(message->{
            logger.info("start handle message");
            dotest(message);
            
        });
        

    }
    
  
    private void dotest( Message<User> message ) {
        System.out.println("srart doselect"+message.body().getMsisdn());
        Userinfo user1=new Userinfo();
        client.getConnection(res -> {
        if (res.succeeded()) {
        SQLConnection connection = res.result();         
        String stmt =  "select user from dual";
//        String stmt =  "select PARTITION_ID,USER_ID,DUMMY_TAG,NET_TYPE_CODE,SERIAL_NUMBER,EPARCHY_CODE,CITY_CODE,CUST_ID,USECUST_ID,BRAND_CODE,PRODUCT_ID,USER_TYPE_CODE,PREPAY_TAG,SERVICE_STATE_CODE,OPEN_MODE,ACCT_TAG,REMOVE_TAG,CREDIT_CLASS,BASE_CREDIT_VALUE,CREDIT_VALUE,CREDIT_CONTROL_ID,SCORE_VALUE,UPDATE_DEPART_ID,UPDATE_STAFF_ID,USER_PASSWD,OPEN_DEPART_ID,PROVINCE_CODE from ucr_act1.TF_F_USER where net_type_code = '50'  and serial_number =?";
        logger.info("stmt :"+stmt);
        connection.query(stmt, resultSet -> {
                 if (resultSet.succeeded()) {
                     System.out.println("excute query");
                     ResultSet rs = resultSet.result();
                     user1.setUser_id(rs.getRows().get(0).getString("USER"));
                     
                     message.reply(user1);
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
    
    private void doSelect( Message<JsonObject> message ) {
        System.out.println("srart doselect"+message.body().getString("msisdn"));
        
        client.getConnection(res -> {
        if (res.succeeded()) {
        SQLConnection connection = res.result();         
        String stmt =  "select user from dual";
//        String stmt =  "select PARTITION_ID,USER_ID,DUMMY_TAG,NET_TYPE_CODE,SERIAL_NUMBER,EPARCHY_CODE,CITY_CODE,CUST_ID,USECUST_ID,BRAND_CODE,PRODUCT_ID,USER_TYPE_CODE,PREPAY_TAG,SERVICE_STATE_CODE,OPEN_MODE,ACCT_TAG,REMOVE_TAG,CREDIT_CLASS,BASE_CREDIT_VALUE,CREDIT_VALUE,CREDIT_CONTROL_ID,SCORE_VALUE,UPDATE_DEPART_ID,UPDATE_STAFF_ID,USER_PASSWD,OPEN_DEPART_ID,PROVINCE_CODE from ucr_act1.TF_F_USER where net_type_code = '50'  and serial_number =?";
        logger.info("stmt :"+stmt);
        connection.query(stmt, resultSet -> {
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
    
    
    private void doinsertnum( Message<JsonObject> message ) {
        System.out.println("srart do insert"+message.body().getString("msisdn"));
        String partition_id=message.body().getString("partition_id");
        String user_id=message.body().getString("user_id");
        client.getConnection(res -> {
        if (res.succeeded()) {
            SQLConnection connection = res.result();         
            String stmt =  "INSERT INTO UCR_ACT1.vertx_test2(partition_id,user_id) VALUES ('"+partition_id+"','"+user_id+"')"; 
            System.out.println("insert sql is :"+stmt+" partition_id:"+partition_id+" user_id:"+user_id);
            connection.update(stmt, res2 -> {
                     if (res2.succeeded()) {
                        System.out.println("excute query");
                        UpdateResult result = res2.result();
                        System.out.println("Updated no. of rows: " + result.getUpdated());
                        System.out.println("Generated keys: " + result.getKeys());
                        message.reply(result.getUpdated());
                        connection.close(res3->{
                            if (res3.succeeded()) {
                                System.out.println("close successful");
                            }else {
                                System.out.println("close fail");
                            }
                        });
                     }else {
                         System.out.println(res2.cause());
                         connection.close(res4->{
                             if (res4.succeeded()) {
                                 System.out.println("close successful");
                             }else {
                                 System.out.println("close fail");
                             }
                         });
                        System.out.println("excute failed1");
                        message.reply("insert failed");
                    }

                 });

            } else {
                System.out.println("excute failed2");
                message.reply("insert failed");
            }
            System.out.println(" I am here");
        });
      }
    
    private void doinsert( Message<JsonObject> message ) {
        logger.info("srart do insert"+message.body().getString("user_id"));
        String partition_id=message.body().getString("partition_id");
        String user_id=message.body().getString("user_id");
        client.getConnection(res -> {
        if (res.succeeded()) {
            SQLConnection connection = res.result();         
            String stmt =  "INSERT INTO UCR_ACT1.vertx_test2 VALUES ("+partition_id+","+user_id+")"; 
            logger.info("insert sql is :"+stmt);
            connection.update(stmt, res2 -> {
                     if (res2.succeeded()) {
                         logger.info("excute query");
                        UpdateResult result = res2.result();
                        logger.info("Updated no. of rows: " + result.getUpdated());
                        logger.info("Generated keys: " + result.getKeys());
                        message.reply(result.getUpdated());
                        connection.close(res3->{
                            if (res3.succeeded()) {
                                logger.info("close successful");
                            }else {
                                logger.error("close fail"+res3.toString());
                            }
                        });
                     }else {
                         logger.error("excute failed1:"+stmt+res2.cause().getLocalizedMessage());
                         res2.cause().printStackTrace();
                         connection.close(res4->{
                             if (res4.succeeded()) {
                                 logger.info("close successful");
                             }else {
                                 logger.info("close fail");
                             }
                         });
                         logger.info("excute failed1");
                        message.reply("insert failed");
                    }

                 });

            } else {
                System.out.println("excute failed2");
                logger.info("insert failed");
                message.reply("insert failed");
            }
            logger.info(" I am here");
        });
      }
    
    public static void main(String[] args){
//        Vertx vertx = Vertx.vertx();
//        vertx.deployVerticle(new oraclebus());            
        DeploymentOptions options = new DeploymentOptions().setInstances(2);
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle("com.cucc.vertx.demo.envetbusoracle.oraclebus",options);
        vertx.deployVerticle(new oracleSender());        
    }
    
}
