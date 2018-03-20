import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import Ticket

class Buyer extends AbstractVerticle {

  @Override
  void start() throws Exception {
    EventBus eb = vertx.eventBus();
    super.start()
    println "Init Buyer"
    Ticket ticket = new Ticket(status: "view", place:"1L")
  //  vertx.setPeriodic(1000, { v ->
      eb.send("com.ticket.office", ticket){ reply ->
        if(reply.succeeded()){
          println "Hizo una compra....."
        }
        else {
          println "No reply"
        }
      }
 //   })
  }
}
