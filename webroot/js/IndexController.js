'use strict';

var IndexController = (function(){
    var start = function(){
    console.log("Start index controller  js");
    var varticleManager = VerticleManager.getInstance();
 
    };

    var viewTicket = function(){
      var onSucces;
      onSucces = (msg) => {
        return $(`#${msg.nameForCounter}`).text(msg.number);
      };
      verticleManager.consumer("mx.makingdevs.comunicate.response", onSucces)
    };
    return{
         start:start
    }

}());
jQuery(function($){
  IndexController.start();
});