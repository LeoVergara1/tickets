
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import Ticket
import Transform
import io.vertx.core.json.JsonArray

class SessionTimeVerticle extends AbstractVerticle {

  @Override
  void start() throws Exception {
    super.start()
    def deployId = deploymentID()
    def sd = vertx.sharedData
    EventBus eb = vertx.eventBus()
    eb.consumer("com.ticket.session.time.${deployId}"){ message ->
      println "Inicia Session time "
      long seconds = 20
      def timerID = vertx.setPeriodic(1000, { id ->
        seconds -=1
        def mapTicket = Transform.mapFromBodyJson(message.body())
        mapTicket.put("seconds", seconds.toString())
        println "And every second this is printed"
        if (seconds == 0){
          println "Se termino tu tiempo de sesión"
          eb.send("com.ticket.cancel", message.body())
          eb.publish("com.makingdevs.comunicate.time.session.${mapTicket.processId}", mapTicket)
          vertx.cancelTimer(id)

        }
        else {
          println "Mesaje al segundero"
          eb.publish("com.makingdevs.comunicate.time.session.${mapTicket.processId}", mapTicket)
        }
      })
      message.reply(timerID)
    }    

  }

  @Override
  void stop() throws Exception {
    
  }
}