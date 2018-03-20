import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus

class Buyer extends AbstractVerticle {

  @Override
  void start() throws Exception {
    EventBus eb = vertx.eventBus();
    super.start()
    println "Init Buyer"
  }
}
