package com.github.cb372.imperials.client.actor

import akka.actor.Actor
import akka.event.Logging
import com.github.cb372.imperials.client.actor.Messages.{NotifyDashboard, FetchResult}
import dispatch._

/**
 * Author: chris
 * Created: 3/3/13
 */
class DashboardNotifier(dashboardUrl: String) extends Actor {
  val log = Logging(context.system, this)


  def receive = {
    case NotifyDashboard(FetchResult(fetchTime, json)) => {
      val request = url(dashboardUrl).POST << json
      Http(request OK as.String).onSuccess {
        case _ => log.info("Sent metrics info to dashboard")
      } onFailure {
        case ex => log.info("Failed to send metrics info to dashboard. Maybe it's not running?")
      }
    }
  }
}
