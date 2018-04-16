(function() {
  VerticleManager = (function() {
    var VerticleManagerPrivate, instance;

    class VerticleManager {
      static getInstance() {
        return instance = new VerticleManagerPrivate();
      }

    };

    instance = null;

    VerticleManagerPrivate = class VerticleManagerPrivate {
      constructor() {
        this.send = this.send.bind(this);
        this.consumer = this.consumer.bind(this);
        console.log("Inicia constructor de Verticle Manager");
        this.urlPath = window.location.href.replace(/(:)\d+\S+/, ""); //#TODO: La url en rara ocación deniega el acceso 
        //this.urlPath2 = $("#urlApplicationFull")[0].href.replace(/(:)\d+\S+/, "");
        this.eventBus = new EventBus(`${this.urlPath}:8080/eventbus/`);
      }

      send(address, msg) {
        return this.eventBus.send(`${address}`, `${msg}`);
      }

      consumer(address, onSucess) {
        return this.eventBus.onopen = () => {
          return this.eventBus.registerHandler(`${address}`, {
            'Access-Control-Allow-Origin': 'Access-Control-Allow-Origin'
          }, function(err, msg) {
            console.log(`Regreso la respuesta ${msg.body} `);
            return onSucess(msg.body);
          });
        };
      }

    };

    return VerticleManager;

  }).call(this);

}).call(this);