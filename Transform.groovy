
import io.vertx.core.json.JsonObject
import groovy.json.*

class Transform {

  static JsonObject gatMapWithNameToSharedMapAndObject(Map map){
    new JsonObject(map)
  }

  static String getProcessId(){
    int number = Math.abs(new Random().nextInt() % 600 + 1)
    "process${number}"
  }

  static JsonObject getJsonObjectFromClass(def object){
    new JsonObject(new JsonBuilder(object).toPrettyString())
  }


  static Map mapFromBodyJson(def map){
    Map newMap = [:]
    map.each{ k, v ->
      newMap.put(k,v)
    }
    newMap
  }

  static def getJsonFromString(String message){
    def jsonSlurper = new JsonSlurper()
    def jsonMap = jsonSlurper.parseText(message)
    jsonMap
  }

}

