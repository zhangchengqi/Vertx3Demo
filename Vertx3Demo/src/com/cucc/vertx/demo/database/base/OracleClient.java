package com.cucc.vertx.demo.database.base;


import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

public class OracleClient {
    private Logger logger=LoggerFactory.getLogger(OracleClient.class);
    private ConcurrentHashMap<String, JDBCClient> pools = new ConcurrentHashMap<String, JDBCClient>();
    //private Hashtable<String,JDBCClient> pools = new Hashtable<String,JDBCClient>();
    static private OracleClient instance;
    public static JsonObject configofngact = new JsonObject()
    .put("url", "jdbc:oracle:thin:@10.124.0.39:1521:ngact")
    .put("driver_class", "oracle.jdbc.driver.OracleDriver")
    .put("user", "ubak").put("password", "ubak_123")
    .put("initial_pool_size", 5)
    .put("min_pool_size", 5)
    .put("max_pool_size", 30);
    
    public static JsonObject configofact1 = new JsonObject()
    .put("url", "jdbc:oracle:thin:@10.124.0.42:1521:tact1")
    .put("driver_class", "oracle.jdbc.driver.OracleDriver")
    .put("user", "uop_act1").put("password", "uop_act1")
    .put("initial_pool_size", 8)
    .put("min_pool_size", 8)
    .put("max_pool_size", 30);
    
    /* 测试营业库
     *UOP_CRM1/uop_crm1@10.124.0.40/tcrm1*/
    public static JsonObject configoftcrm1 = new JsonObject()
    .put("url", "jdbc:oracle:thin:@10.124.0.40:1521:tcrm1")
    .put("driver_class", "oracle.jdbc.driver.OracleDriver")
    .put("user", "UOP_CRM1").put("password", "uop_crm1")
    .put("initial_pool_size", 8)
    .put("min_pool_size", 8)
    .put("max_pool_size", 30);
    
    
    
    public static OracleClient getInstance(Vertx vertx) {
        if (instance == null) {
            instance = new OracleClient(vertx);
        }
        return instance;
    }
    
    private OracleClient(Vertx vertx) {
        this.init(vertx);
    }
    
    private void init(Vertx vertx) {
        logger.info("创建数据库连接池。。。");
        createPools(vertx);
        logger.info("创建数据库连接池完毕。。。");
    }
    
    private void createPools(Vertx vertx) {
       JDBCClient clientofngact=JDBCClient.createShared(vertx, configofngact, "ngact");
       JDBCClient clientofact1=JDBCClient.createShared(vertx, configofact1, "act1");
       JDBCClient clientoftcrm1=JDBCClient.createShared(vertx, configoftcrm1,"tcrm1");
       pools.put("ngact", clientofngact);
       pools.put("act1", clientofact1);
       pools.put("tcrm1", clientoftcrm1);
       
    }
    
    public JDBCClient getClient(String name) {        
        return pools.get(name);
    }

}

