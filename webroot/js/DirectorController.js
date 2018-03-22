'use strict';

var DirectorController = (function(){
    var routes;
    var start = function(){
    console.log("Start controller director js");
    routes = {
        '/buy/?([^\/]*)\/?': console.log("Comprando"),
        '/view/?([^\/]*)\/?': console.log("Viendo")
      };
      return urlMappings()
    };

    var urlMappings = function (){
        var router;
        router = Router(routes);
        console.log("Iniciando rutas")
        return router.init();
        
    };

    return{
         start:start
    }

}());
jQuery(function($){
    DirectorController.start();
});