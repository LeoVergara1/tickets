
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import Ticket
import Transform
import io.vertx.core.json.JsonArray

class Buyer extends AbstractVerticle {

  @Override
  void start() throws Exception {
    super.start()
    def processId = Transform.getProcessId()
    def sd = vertx.sharedData
    EventBus eb = vertx.eventBus();
    println "Init Buyer"
    Ticket ticket = new Ticket(status: "bought", place:"2L", id: processId)
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

    eb.consumer("com.ticket.status.${processId}"){ message ->
      println message.body()
    }
  }

  @Override
  void stop() throws Exception {
    def sd = vertx.sharedData
    def result = sd.getLocalMap("result")
    JsonArray places = result.placesInVeiw
    places.remove("2L")
  }
}
