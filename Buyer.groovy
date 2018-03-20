import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus

class Buyer extends AbstractVerticle {

  @Override
  void start() throws Exception {
    EventBus eb = vertx.eventBus();
    super.start()
    println "Init Buyer"
    vertx.setPeriodic(1000, { v ->
      eb.send("com.ticket.office", [status:"view", id:"2"]){ reply ->
        if(reply.succeeded()){
          println "Hizo una compra....."
        }
        else {
          println "No reply"
        }
      }
    })
  }
}
