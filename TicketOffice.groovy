import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import Ticket
import Transform

class TicketOffice extends AbstractVerticle {


  @Override
  void start() throws Exception {
    def sd = vertx.sharedData
    def result = sd.getLocalMap("result")
    result.placesInVeiw = new JsonArray()
    EventBus eb = vertx.eventBus();
    super.start()
    println "Init Ticket Office"
    eb.consumer("com.ticket.office"){ message ->
      JsonArray places = result.placesInVeiw
      places.add(message.body().place)
      if(result.placesInVeiw.contains(message.body().place)){
        message.reply("Tu lugar es visto por alguien más")
      }
      else {
        message.reply("Eres el primero viendo el lugar")
      }
      result.placesInVeiw = places
      println result.placesInVeiw.dump()
      println("Received message: ${message.body()}")

      if(message.body().status == "bought"){
        eb.send("com.ticket.status.${message.body().place}", "Compraron tu boleto")
      }
    }


  }


}
