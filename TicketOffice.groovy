
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
      JsonArray process
      (placeCurrent.process) ? (process = placeCurrent.process) : (process = new JsonArray()) 
      process.add(message.body().client)
      println "Processos: ${process}"
      placeCurrent.process = process 
      JsonArray places = result.placesInVeiw
      places.add(message.body().place)
      if(result.placesInVeiw.contains(message.body().place)){
        message.reply([message:"Tu lugar es visto por alguien más", count: process.size()])
      }
      else {
        message.reply([message:"Eres el primero viendo el lugar" , count: process.size()])
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

    eb.consumer("com.makingdevs.comunicate.back"){ message ->
      println "conusmer test to comunicate from front"
    
    }

    eb.consumer("com.ticket.office.buy"){ message ->
      println "Se ha comprado el boleto ${message.body().place}"
      def placeCurrent = sd.getLocalMap("${message.body().place}") 
      placeCurrent.process.each{
        println "cliente: ${it}"
        eb.send("com.ticket.comunicate.status.${it}", "Compraron tu boleto")
      }
    }

    eb.consumer("com.ticket.cancel"){ message ->
      println "Se ha liberado  el boleto ${message.body().place}"
      def placeCurrent = sd.getLocalMap("${message.body().place}") 
      JsonArray process = placeCurrent.process
      process.remove(message.body().client)
      placeCurrent.process = process
      println "Cliente a quitar: ${message.body().client}"
      vertx.undeploy(message.body().client)
      eb.publish("com.makingdevs.comunicate.info.${message.body().ticket}", [status: "Liberaste el boleto", count: placeCurrent.process.size()])
    }


  }


}
