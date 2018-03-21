
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
      def placeCurrent = sd.getLocalMap("${message.body().place}")  
      (placeCurrent.count) ? (placeCurrent.count +=1) : (placeCurrent.count = 0)
      JsonArray process = placeCurrent.process
      process.add(message.body().processId)
      placeCurrent.process = process 
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
        placeCurrent.process.each{
          eb.send("com.ticket.status.${it}", "Compraron tu boleto")
        }
      }
    }


  }


}
