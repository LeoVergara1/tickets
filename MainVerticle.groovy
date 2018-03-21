package com.makingdevs.ticket

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions

class MainVerticle extends AbstractVerticle {
  @Override
  void start(){
    def config = vertx.currentContext().config()
    DeploymentOptions options = new DeploymentOptions().setConfig(config)
    vertx.deployVerticle("Webserver.groovy", options)
  }
}