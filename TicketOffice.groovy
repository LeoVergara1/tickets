import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus

class TicketOffice extends AbstractVerticle {


  @Override
  void start() throws Exception {
    EventBus eb = vertx.eventBus();
    super.start()
    println "Init Ticket Office"
    eb.consumer("ticketOffice"){ message ->
      println("Received message: ${message.body()}")
      // Now send back reply
      message.reply("pong!")
    }
  }


}
