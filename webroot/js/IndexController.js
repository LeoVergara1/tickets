'use strict';

var IndexController = (function(){
    var start = () => {
    console.log("Start index controller  js EMC6s");
     bindEvents() 
    };

    var bindEvents = ()=> {
      viewTicket()
    };

    var viewTicket = ()=> {
      var onSucces;
      var varticleManager = VerticleManager.getInstance();
      onSucces = (msg) => {
        return $(`#${msg.nameForCounter}`).text(msg.number);
      };
      varticleManager.consumer("com.makingdevs.comunicate.response", onSucces)
    };
    return{
         start:start
    }

}());
jQuery(function($){
  IndexController.start();
});