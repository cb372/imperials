package com.github.cb372.imperials.client.actor

import akka.actor.{Props, Actor}
import com.mongodb.casbah.{MongoCollection, MongoClient}
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
  val mongoWriter = context.actorOf(Props(new MongoWriter(mongo)), s"${host}-mongo-writer")
  val metricsFetcher = context.actorOf(Props(new MetricsFetcher(metricsUrl)), s"${host}-metrics-fetcher")
  val dashboardNotifier = dashboardNotifyUrl map { url =>
    context.actorOf(Props(new DashboardNotifier(url)), s"${host}-dashboard-notifier")
  }

  def receive = {
    case Poll => metricsFetcher ! Fetch
    case Fetched(fetchResult) => {
      mongoWriter ! WriteToMongo(fetchResult)
      dashboardNotifier map { worker => worker ! NotifyDashboard(fetchResult) }
    }
  }
}
