
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import io.vertx.core.DeploymentOptions
import Ticket
import Transform
import io.vertx.core.json.JsonArray

class ComunicateVerticle extends AbstractVerticle {

  @Override
  void start() throws Exception {
    super.start()
    DeploymentOptions opts_1 = new DeploymentOptions()
    opts_1.setWorker(true)
    def sd = vertx.sharedData
    def result = sd.getLocalMap("result")
    EventBus eb = vertx.eventBus();
    
    eb.consumer("com.makingdevs.comunicate.send.view"){ message ->
      println message.body()
      println "Estas aquí"
      def jsonMap = Transform.getJsonFromString(message.body())
      vertx.deployVerticle("Buyer.groovy", opts_1) { resultVerticle ->
        if(resultVerticle.succeeded()){
          println("The verticle has been deployed, deployment ID is " + resultVerticle.result())
          eb.send("com.ticket.init.vew.${resultVerticle.result()}", jsonMap)
          eb.publish("com.makingdevs.comunicate.response.${jsonMap.processId}", [ deployMentId :resultVerticle.result()])
        }
        else{
          resultVerticle.cause().printStackTrace()
        }
      }
    }
    eb.consumer("com.makingdevs.comunicate.send.buy"){ message ->
      println message.body()
      println "Comprando...."
      def jsonMap = Transform.getMapFromString(message.body())
      eb.send("com.ticket.status.${jsonMap.deployMentId}", jsonMap){ reply ->
        if(reply.succeeded()){
          println "Respuesta: ${reply.result().body()} "
          def response = Transform.mapFromBodyJson(reply.result().body())
          response.put("clientBuy","${jsonMap.processId}")
          vertx.cancelTimer(jsonMap.idTimer.toLong())
          eb.publish("com.makingdevs.comunicate.info.buy.${reply.result().body().place}", response )
        }
      }
    }

    eb.consumer("com.makingdevs.comunicate.send.cancel"){ message ->
      println message.body()
      def map = Transform.getMapFromString(message.body())
      map.put("place", map.ticket)
      map.put("client", map.deployMentId)
      eb.send("com.ticket.cancel", map)
      vertx.cancelTimer(map.idTimer.toLong())
    }

   eb.consumer("com.makingdevs.comunicate.send.test"){ message ->
    println message.body()
    eb.publish("com.makingdevs.comunicate.consumer.test", "Te regreso la respuesta")
   } 
  }

  @Override
  void stop() throws Exception {
   
  }
}