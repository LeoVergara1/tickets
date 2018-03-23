
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import Ticket
import Transform
import io.vertx.core.json.JsonArray
import io.vertx.core.DeploymentOptions

class Buyer extends AbstractVerticle {

  @Override
  void start() throws Exception {
    super.start()
    def processId = deploymentID()
    println "Proceso del cliente: ${processId}"
    def sd = vertx.sharedData
    EventBus eb = vertx.eventBus()
    println "Init Buyer"
    eb.consumer("com.ticket.init.vew.${processId}"){ message ->
      println "Inicia proceso de vista con el boleto: ${message.body().ticket}"
      Ticket ticket = new Ticket(status:"view", place:message.body().ticket, client: processId)
      def jsonTicket = Transform.getJsonObjectFromClass(ticket)
      eb.send("com.ticket.office", jsonTicket){ reply ->
        println reply.result().body()
        def mapFromFont = Transform.mapFromBodyJson(message.body())
        mapFromFont.put("client", processId)
        mapFromFont.put("place", message.body().ticket)
        String deployIdTime = deploytmentSessionTime(mapFromFont)
        println "Map desde el front: ${mapFromFont}"
        eb.publish("com.makingdevs.comunicate.info.${message.body().ticket}", [status: reply.result().body().message, count:reply.result().body().count])
      }
    }

    eb.consumer("com.ticket.status.${processId}"){ message ->
      Ticket ticket = new Ticket(status:"bought", place:message.body().ticket, client: processId)
      def jsonTicket = Transform.getJsonObjectFromClass(ticket)
      println message.body()
      eb.send("com.ticket.office.buy", jsonTicket){ reply ->
        
      }
      message.reply(jsonTicket)
    }
  }

  @Override
  void stop() throws Exception {
    def sd = vertx.sharedData
    def result = sd.getLocalMap("result")
  }

  private def deploytmentSessionTime(def jsonMap){
    DeploymentOptions opts_1 = new DeploymentOptions()
    opts_1.setWorker(true)
    def sd = vertx.sharedData
    EventBus eb = vertx.eventBus()
    String deployIdTime
    vertx.deployVerticle("SessionTimeVerticle.groovy", opts_1){ resultVerticle ->
      if (resultVerticle.succeeded()){
        deployIdTime = resultVerticle.result()
        println "Id timer: ${deployIdTime}"
        eb.send("com.ticket.session.time.${deployIdTime}", jsonMap)
      }
      else {
        resultVerticle.cause().printStackTrace()
      }
    }
    deployIdTime
  }
}
