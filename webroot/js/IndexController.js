'use strict';

var IndexController = (function(){
  var varticleManagerSend 
  var process = Math.floor((Math.random() * 10000) + 1);
  var deployMentId
  var firtsTicket = "L1"
  console.log(`Proceso: ${process}`)
    var start = () => {
    varticleManagerSend = VerticleManager.getInstance();
    console.log("Start index controller  js EMC6s");
    statusTicket(firtsTicket)
    bindEvents() 
    };

    var bindEvents = ()=> {
      $("#buttonCancel").hide()
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
        console.log("Siempre regresa")
        $("#buttonCancel").show()
        varticleManagerSend.send("com.makingdevs.comunicate.send.view", `{ \"ticket\": \"${ticket}\", \"processId\": \"${process}\"}`)
      })
      
      $("#buttonBuy").on("click" , () => {
        console.log("Click buton comprar")
        let ticket = $('#sel1 option:selected').val()
        console.log(`Boleto seleccionado: ${ticket}`)
        varticleManagerSend.send("com.makingdevs.comunicate.send.buy", `{ \"ticket\": \"${ticket}\", \"processId\": \"${process}\" , \"deployMentId\" : \"${deployMentId}\" }`)
      })
      $("#sel1").on("change", () => {
        console.log("Deploy consumer")
        let ticket = $('#sel1 option:selected').val()
        statusTicket(ticket);
      })
      $("#buttonCancel").on("click", () =>{
        console.log("Liberando compra")
        let ticket = $('#sel1 option:selected').val()
        varticleManagerSend.send("com.makingdevs.comunicate.send.cancel", `{ \"ticket\": \"${ticket}\", \"processId\": \"${process}\"}`)
      })
    };
    var sleep = (ms) =>{
      return new Promise(resolve => setTimeout(resolve, ms));
    };

    var viewTicket = ()=> {
      var onSucces;
      var varticleManager = VerticleManager.getInstance();
      onSucces = (msg) => {
        $("#buttonView").hide()
        $('#sel1').prop('disabled', true);
        deployMentId = msg.deployMentId
        return $(`#${msg.idDeploy}`).text(msg.number);
      };
      varticleManager.consumer(`com.makingdevs.comunicate.response.${process}`, onSucces)
    };
    
    var statusTicket = (ticket)=> {
      var onSucces;
      var varticleManagerStatus = VerticleManager.getInstance();
      onSucces = (msg) => {
        $("#information").text(msg.status)
        $("#informationCounter").text(msg.count)
        return $(`#${msg.idDeploy}`).text(msg.number);
      };
      varticleManagerStatus.consumer(`com.makingdevs.comunicate.info.${ticket}`, onSucces)
    };

    var informationTicket= (ticket) => {
      var onSucces;
      var varticleManagerInfo = VerticleManager.getInstance();
      onSucces = (msg) => {
        console.log(`Status: ${msg}`)
        console.log(msg)
        console.log("Boleto Comprado")
        console.log(msg.clientBuy)
        console.log(process)
        if(msg.clientBuy === `${process}`){
          console.log("El compro el boleto************")
          $.notify("Compra exitosa", "info");
        }
        else {
          $.notify("Compraron tu boleto");
        }
        $("#buttonBuy").hide()
      };
      varticleManagerInfo.consumer(`com.makingdevs.comunicate.info.buy.${ticket}`, onSucces)

    };
    return{
         start:start
    }

}());
jQuery(function($){
  IndexController.start();
});