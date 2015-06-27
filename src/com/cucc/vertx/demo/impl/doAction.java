package com.cucc.vertx.demo.impl;

import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import com.cucc.vertx.demo.database.base.OracleClient;
import com.cucc.vertx.demo.object.User;


public class doAction {
    
    public JDBCClient jdbcClient;
    
    public User getUser(Vertx vertx){
        jdbcClient=OracleClient.getInstance(vertx).getClient("ngact");
        User user=new User();     
        jdbcClient.getConnection(res -> {
            if (res.succeeded()) {
                SQLConnection connection = res.result();                
                String stmt = "select user from dual";                
                connection.query(stmt,resultSet->{                   
                    if (resultSet.succeeded()) {
                        System.out.println("excute query");
                        ResultSet rs = resultSet.result();
                        user.setUser_id(rs.getRows().get(0).getString("USER"));                                               
                       }else {
                           System.out.println("excute failed1"+resultSet.cause());
                       }
                    
                });
                String stmt2 = "select name from *** where user='"+user.getUser_id()+"'"; 
                connection.query(stmt2, resultSet2->{                   
                    if (resultSet2.succeeded()) {
                        System.out.println("excute query");
                        ResultSet rs = resultSet2.result();
                        user.setMsisdn(rs.getRows().get(0).getString("NAME"));                                               
                       }else {
                           System.out.println("excute failed1"+resultSet2.cause());
                       }

                    
                });
                connection.close(release->{
                    if (release.succeeded()) {
                        System.out.println("close successful");
                   }else {
                       System.out.println("close fail"+release.cause());
                   }
                });
            } else {
                // Failed to get connection - deal with it
                res.cause().printStackTrace();
                
            }
        });
        return user;
    }

}
