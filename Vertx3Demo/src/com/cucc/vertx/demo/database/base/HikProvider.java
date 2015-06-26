package com.cucc.vertx.demo.database.base;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.spi.DataSourceProvider;

public class HikProvider implements DataSourceProvider {
    @Override
    public DataSource getDataSource(JsonObject config) throws SQLException {
      String url = config.getString("url");
      if (url == null) throw new NullPointerException("url cannot be null");
      String user = config.getString("user");
      String password = config.getString("password");
      Integer maxPoolSize = config.getInteger("max_pool_size");      
      HikariConfig configofhk = new HikariConfig();
      configofhk.setMaximumPoolSize(maxPoolSize);                                        
      String connectionTestQuery = "select 1 from dual";
      configofhk.setConnectionTestQuery(connectionTestQuery );
//      configofhk.setDriverClassName("oracle.jdbc.pool.OracleDataSource");
      configofhk.setJdbcUrl(url);
      configofhk.addDataSourceProperty("user", user);
      configofhk.addDataSourceProperty("password", password);                    
      HikariDataSource ds = new HikariDataSource(configofhk);         
      return ds;
    }

    @Override
    public void close(DataSource dataSource) throws SQLException {
      if (dataSource instanceof HikariDataSource) {
        ((HikariDataSource) dataSource).close();
      }
    }
}
