import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import Ticket
import Transform
import io.vertx.core.json.JsonArray

class Buyer extends AbstractVerticle {

  @Override
  void start() throws Exception {
    def sd = vertx.sharedData
    EventBus eb = vertx.eventBus();
    super.start()
    println "Init Buyer"
    Ticket ticket = new Ticket(status: "view", place:"2L")
    def jsonTicket = Transform.getJsonObjectFromClass(ticket)
  //  vertx.setPeriodic(1000, { v ->
      eb.send("com.ticket.office", jsonTicket){ reply ->
        if(reply.succeeded()){
          println reply.result().body()
        }
        else {
          println "No reply"
        }
      }
 //   })

    eb.consumer("com.ticket.status"){ message ->
      println "Usuarios con el mismo lugar: "
    }
  }
}
