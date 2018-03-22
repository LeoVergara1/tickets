
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import Ticket
import Transform
import io.vertx.core.json.JsonArray

class Buyer extends AbstractVerticle {

  @Override
  void start() throws Exception {
    super.start()
    def processId = deploymentID()
    println "Proceso del cliente: ${processId}"
    def sd = vertx.sharedData
    EventBus eb = vertx.eventBus();
    println "Init Buyer"
    eb.consumer("com.ticket.init.vew.${processId}"){ message ->
      println "Inicia proceso de vista con el boleto: ${message.body().ticket}"
      Ticket ticket = new Ticket(status:"view", place:message.body().ticket, client: processId)
      def jsonTicket = Transform.getJsonObjectFromClass(ticket)
      eb.send("com.ticket.office", jsonTicket){ reply ->
        println reply.result().body()
        eb.send("com.makingdevs.comunicate.info.${message.body().processId}", reply.result().body())
      }
    }

    eb.consumer("com.ticket.status.${processId}"){ message ->
      Ticket ticket = new Ticket(status:"bought", place:message.body().ticket, client: processId)
      def jsonTicket = Transform.getJsonObjectFromClass(ticket)
      println message.body()
      eb.send("com.ticket.office.buy", jsonTicket){ reply ->
        println reply.result().body()
      }
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
