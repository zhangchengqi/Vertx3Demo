package com.cucc.vertx.demo.database.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

public class HkDataClient extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(HkDataClient.class);
    public JsonObject config = new JsonObject()
    .put("provider_class", "com.cucc.vertx.demo.database.base.HikProvider")
    .put("url", "jdbc:oracle:thin:@10.124.0.42:1521:tact1")
//    .put("url", "jdbc:oracle:thin:@192.168.160.100:1521:orcl")
//    .put("driver_class", "oracle.jdbc.pool.OracleDataSource")
    .put("user", "uop_act1").put("password", "uop_act1")
//     .put("user", "ucr_act1").put("password", "ucr_act1")
    .put("initial_pool_size", 5)
    .put("min_pool_size", 5)
    .put("max_pool_size", 30);
    
    public JDBCClient client;
    public void start(){
        logger.info("start work");
        client = JDBCClient.createShared(vertx, config);
        for (int i = 0; i < 10000; i++) {
        client.getConnection(res->{        
            if (res.succeeded()) {
            SQLConnection connection = res.result();         
            String stmt =  "select user from dual";
            logger.info("stmt :"+stmt);

                

            connection.query(stmt, resultSet -> {
                     if (resultSet.succeeded()) {
                         System.out.println("excute query");
                         ResultSet rs = resultSet.result();
                         System.out.println("hhhhhhhhhhhh"+rs.getRows());
                         
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
    public static void main(String[] args){

      Vertx vertx = Vertx.vertx();
      vertx.deployVerticle(new HkDataClient());      
  }

}
