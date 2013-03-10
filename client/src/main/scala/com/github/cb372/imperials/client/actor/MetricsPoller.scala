package com.github.cb372.imperials.client.actor

import akka.actor.{Props, Actor}
import com.mongodb.casbah.MongoCollection
import com.github.cb372.imperials.client.actor.Messages._
import com.github.cb372.imperials.client.actor.Messages.Fetched
import com.github.cb372.imperials.client.actor.Messages.WriteToMongo

/**
 * Author: chris
 * Created: 3/3/13
 */
class MetricsPoller(host: String,
                    metricsUrl: String,
                    mongo: MongoCollection,
                    dashboardNotifyUrl: Option[String]) extends Actor {
  val metricsFetcher = context.actorOf(Props(new MetricsFetcher(host, metricsUrl)), s"${host}-metrics-fetcher")
  val jsonRewriter = context.actorOf(Props[JsonRewriter], s"${host}-json-rewriter")
  val mongoWriter = context.actorOf(Props(new MongoWriter(mongo)), s"${host}-mongo-writer")
  val dashboardNotifier = dashboardNotifyUrl map { url =>
    context.actorOf(Props(new DashboardNotifier(url)), s"${host}-dashboard-notifier")
  }

  def receive = {
    case Poll => metricsFetcher ! Fetch
    case Fetched(fetchResult) => jsonRewriter ! RewriteJson(fetchResult)
    case Rewrote(fetchResult) => {
      mongoWriter ! WriteToMongo(fetchResult)
      dashboardNotifier map { worker => worker ! NotifyDashboard(fetchResult) }
    }
  }
}
