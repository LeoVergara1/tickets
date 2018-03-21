@Grapes(
  [@Grab(group='org.apache.ivy', module='ivy', version='2.1.0'),
  @Grab(group='io.vertx', module='vertx-shell', version='3.5.1')]
)
import org.apache.ivy.core.report.ResolveReport
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import Ticket
import Transform
import io.vertx.core.json.JsonArray
import io.vertx.ext.shell.*
import io.vertx.ext.shell.term.*

def service = ShellService.create(vertx, [
telnetOptions:[
  host:"localhost",
  port:3000
  ]
])
service.start();
