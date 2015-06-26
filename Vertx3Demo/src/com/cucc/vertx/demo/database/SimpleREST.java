package com.cucc.vertx.demo.database;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleREST extends AbstractVerticle {

//  private SimpleREST that = this;
  private rest that = new rest();
  private Map<String, JsonObject> products = new HashMap<>();
  private static final Logger logger =LoggerFactory.getLogger(SimpleREST.class);
 // private static Logger logger  = Logger.getLogger(SimpleREST.class);        

  @Override
  public void start() {
    
    setUpInitialData();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());    
    router.get("/products/:productID").handler(that::handleGetProduct);
    router.put("/products/:productID").handler(that::handleAddProduct);
    router.get("/products").handler(that::handleListProducts);

    vertx.createHttpServer().requestHandler(router::accept).listen(8888, "localhost");
  }

  private void handleGetProduct(RoutingContext routingContext) {
    String productID = routingContext.request().getParam("productID");
    String name=routingContext.request().getParam("name");
    HttpServerResponse response = routingContext.response();
    if (productID == null) {
      sendError(400, response);
    } else {
      JsonObject product = products.get(productID);
      if (product == null) {
        sendError(404, response);
      } else {
        logger.info("productID:"+productID);
        response.putHeader("content-type", "application/json").end(product.encode()+name);
      }
    }
  }
  
  private void handleAddProduct(RoutingContext routingContext) {
    String productID = routingContext.request().getParam("productID");
    HttpServerResponse response = routingContext.response();
    if (productID == null) {
      sendError(400, response);
    } else {
      JsonObject product = routingContext.getBodyAsJson();
      if (product == null) {
        sendError(400, response);
      } else {
        logger.info("productID:"+productID);
        products.put(productID, product);
        response.end();
      }
    }
  }

  private void handleListProducts(RoutingContext routingContext) {
    JsonArray arr = new JsonArray();
    products.values().forEach(arr::add);
    routingContext.response().putHeader("content-type", "application/json").end(arr.encode());
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

  private void setUpInitialData() {
    addProduct(new JsonObject().put("id", "12345678").put("name", "Egg Whisk").put("price", 3.99).put("weight", 150));
    addProduct(new JsonObject().put("id", "prod7340").put("name", "Tea Cosy").put("price", 5.99).put("weight", 100));
    addProduct(new JsonObject().put("id", "prod8643").put("name", "Spatula").put("price", 1.00).put("weight", 80));
  }

  private void addProduct(JsonObject product) {
    products.put(product.getString("id"), product);
  }
}