package com.cucc.vertx.demo.clustermanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import com.cucc.vertx.demo.common.HazelcastClusterBuilder;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;

public class VertxServerCluster {
    private static Logger logger = LoggerFactory.getLogger(VertxServerCluster.class);

    public static void main(String[] args) {

//      VertxOptions options = new VertxOptions()
//              .setClustered(true)
//              .setClusterHost("192.168.1.2")
//              .setClusterPort(5701);
//      System.setProperty("vertx.cluster.public.host","192.168.160.102");
//      System.setProperty("vertx.cluster.public.port","30000");
//      Config hazelcastConfig = new Config();
//
//      NetworkConfig network = hazelcastConfig.getNetworkConfig();
//      network.setPort(5701)
//              .setPortAutoIncrement(false)
//              .setPublicAddress("192.168.160.102")
//            //  .addOutboundPortDefinition("3000")
//              .setReuseAddress(true);
//
//      JoinConfig join = network.getJoin();
//      join.getMulticastConfig().setEnabled(false);
//
//      join.getTcpIpConfig()
//              //.setRequiredMember("192.168.160.101") 
//              //.addMember("192.168.160.101")
//              //.addMember("192.168.160.102")
//              .setEnabled(true);
//
//      ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
//
//      VertxOptions options = new VertxOptions()
//              .setClusterManager(mgr)
//              .setClustered(true)
//              .setClusterHost("0.0.0.0")
//              .setClusterPort(30000);
        
        
       VertxOptions options = HazelcastClusterBuilder.create()
                .setClusterPublicAddress("192.168.160.102")
                .setClusterPublicPort(5701, 30000)
                //.addMember("192.168.160.101")
                .build();

      Vertx.clusteredVertx(options, res -> {
          if (res.succeeded()) {
              Vertx vertx = res.result();
              vertx.deployVerticle("com.cucc.vertx.demo.clustermanger.ClusterServer",res2->{
                  if (res2.succeeded()) {
                    logger.info("部署server成功");
                }
              });
          } else {
              logger.info("Cluster server is failed");
          }
      });
  }

}
