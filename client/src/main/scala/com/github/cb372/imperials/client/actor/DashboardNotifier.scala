package com.github.cb372.imperials.client.actor

import akka.actor.Actor
import akka.event.Logging
import com.github.cb372.imperials.client.actor.Messages.{NotifyDashboard, FetchResult}
import dispatch._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._
import org.json4s.ext.DateTimeSerializer

/**
 * Author: chris
 * Created: 3/3/13
 */
class DashboardNotifier(dashboardUrl: String) extends Actor {
  val log = Logging(context.system, this)


  def receive = {
    case NotifyDashboard(FetchResult(host, fetchTime, data)) => {
      implicit val formats = org.json4s.DefaultFormats ++ org.json4s.ext.JodaTimeSerializers.all
      val json = ("host" -> host) ~ ("fetchTime" -> DateTimeSerializer.serialize.apply(fetchTime)) ~ ("data" -> data)
      val request = url(dashboardUrl).POST << compact(render(json))
      Http(request OK as.String).onSuccess {
        case _ => log.info("Sent metrics info to dashboard")
      } onFailure {
        case ex => log.info("Failed to send metrics info to dashboard. Maybe it's not running?")
      }
    }
  }
}
