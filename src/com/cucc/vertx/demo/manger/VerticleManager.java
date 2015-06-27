package com.cucc.vertx.demo.manger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;


public class VerticleManager extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(VerticleManager.class);
    @Override
    public void start() {
        
        deplyOracle();

    }
    
    private void deplyOracle() {
        vertx.deployVerticle("com.cucc.vertx.demo.envetbusoracle.oraclebus", event-> {
                if (event.succeeded()) {
                    logger.info("oraclebus 部署成功, ID is " + event.result());
                    vertx.deployVerticle("com.cucc.vertx.demo.envetbusoracle.oracleSender", event2->{
                        logger.info("oracleSender 部署成功, ID is " + event2.result()); 
                    });
                } else {
                    event.cause().printStackTrace();
                }
        });
    }
    
    


    
}