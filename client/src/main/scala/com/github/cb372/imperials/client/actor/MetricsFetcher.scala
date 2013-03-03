package com.github.cb372.imperials.client.actor

import akka.actor.Actor
import akka.event.Logging
import com.github.cb372.imperials.client.actor.Messages.{FetchResult, Fetched, Fetch}
import dispatch._
import org.joda.time.{DateTimeUtils, DateTime}

/**
 * Author: chris
 * Created: 3/3/13
 */
class MetricsFetcher(metricsUrl: String) extends Actor {
  val metrics = url(metricsUrl)
  val log = Logging(context.system, this)

  def receive = {
    case Fetch => {
      Http(metrics OK as.String).onSuccess[Unit] {
        case json => {
          log.info(s"Fetched metrics info from ${metricsUrl}")
          context.parent ! Fetched(FetchResult(DateTime.now(), json))
        }
      } onFailure {
        case ex => log.error("Failed to fetch metrics info from ${metricsUrl}")
      }
    }
  }
}
