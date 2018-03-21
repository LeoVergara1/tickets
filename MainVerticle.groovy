package com.makingdevs.ticket
//@Grapes(
//    [
//    @Grab(group='io.vertx', module='vertx-core', version='3.5.1'),
//    @Grab(group='io.vertx', module='vertx-web', version='3.5.1'),
//    @Grab(group='io.vertx', module='vertx-ext', version='30', type='pom'),
//    @Grab(group='io.vertx', module='vertx-shell', version='3.5.1'),
//    @Grab(group='io.vertx', module='vertx-dropwizard-metrics', version='3.0.0-milestone6')
//    ]
//)
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.Router
import io.vertx.core.Vertx
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.ext.dropwizard.MetricsService
import io.vertx.core.DeploymentOptions
import io.vertx.ext.shell.ShellService
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.ext.web.handler.sockjs.BridgeOptions
import io.vertx.ext.web.handler.sockjs.PermittedOptions

println "Webserver ok"

DeploymentOptions opts_1 = new DeploymentOptions()
opts_1.setWorker(true)
vertx.deployVerticle("TicketOffice.groovy", opts_1)
def service = MetricsService.create(vertx)
def server = vertx.createHttpServer()
def router = Router.router(vertx)
BridgeOptions bridgeOptions = new BridgeOptions()
bridgeOptions.addInboundPermitted(new PermittedOptions().setAddressRegex("mx.makingdevs.comunicate.*"))
    .addOutboundPermitted(new PermittedOptions().setAddressRegex("mx.makingdevs.comunicate.*"));

def ebHandler = SockJSHandler.create(vertx).bridge(bridgeOptions)

router.route("/eventbus/*").handler(ebHandler)
router.route().handler(StaticHandler.create().setCachingEnabled(false))

Integer counter = 0

vertx.setPeriodic(30000){ t ->
  println "Trabajando.."
  vertx.eventBus().send("com.makingdevs.web.monitor", counter)
  counter++
}

def serviceShell = ShellService.create(vertx,
new ShellServiceOptions().setTelnetOptions(
  new TelnetTermOptions().
  setHost("localhost").
  setPort(4000)
)
);
serviceShell.start();

server.requestHandler(router.&accept).listen(8080)
