'use strict';

var IndexController = (function(){
  var varticleManagerSend 
  var process = Math.floor((Math.random() * 10000) + 1);
  console.log(`Proceso: ${process}`)
    var start = () => {
    varticleManagerSend = VerticleManager.getInstance();
    console.log("Start index controller  js EMC6s");
     bindEvents() 
    };

    var bindEvents = ()=> {
      viewTicket();
      selectPlace();
    };

    var selectPlace = () => {
      console.log("Seleccionar Acientos")
      console.log(varticleManagerSend)
      $("#buttonView").on("click" , () => {
        console.log("Click buton ver")
        let ticket = $('#sel1 option:selected').val()
        console.log(`Boleto seleccionado: ${ticket}`)
        informationTicket(ticket)
        varticleManagerSend.send("com.makingdevs.comunicate.send.view", `{ \"ticket\": \"${ticket}\", \"processId\": \"${process}\"}`)
      })
    };

    var viewTicket = ()=> {
      var onSucces;
      var varticleManager = VerticleManager.getInstance();
      onSucces = (msg) => {
        $("#buttonView").hide()
        $('#sel1').prop('disabled', true);
        return $(`#${msg.idDeploy}`).text(msg.number);
      };
      varticleManager.consumer(`com.makingdevs.comunicate.response.${process}`, onSucces)
    };

    var viewTicket = ()=> {
      var onSucces;
      var varticleManagerInfo = VerticleManager.getInstance();
      onSucces = (msg) => {
        console.log(`Status: ${msg}`)
        $("#information").text(msg)
        return $(`#${msg}`).text(msg);
      };
      varticleManagerInfo.consumer(`com.makingdevs.comunicate.info.${process}`, onSucces)
    };
    var informationTicket= (ticket) => {
      var onSucces;
      var varticleManagerInfo = VerticleManager.getInstance();
      onSucces = (msg) => {
        console.log(`Status: ${msg}`)
        $("#informationCounter").text(msg)
        return $(`#${msg}`).text(msg);
      };
      varticleManagerInfo.consumer(`com.makingdevs.comunicate.info.counter.${ticket}`, onSucces)

    };
    return{
         start:start
    }

}());
jQuery(function($){
  IndexController.start();
});