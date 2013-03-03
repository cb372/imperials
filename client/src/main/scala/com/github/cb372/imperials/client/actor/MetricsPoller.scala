package com.github.cb372.imperials.client.actor

import akka.actor.{Props, Actor}
import com.mongodb.casbah.{MongoCollection, MongoClient}
import com.github.cb372.imperials.client.actor.Messages.{WriteToMongo, Fetched, Fetch, Poll}

/**
 * Author: chris
 * Created: 3/3/13
 */
class MetricsPoller(host: String, metricsUrl: String, mongo: MongoCollection) extends Actor {
  val mongoWriter = context.actorOf(Props(new MongoWriter(mongo)), s"${host}-mongo-writer")
  val metricsFetcher = context.actorOf(Props(new MetricsFetcher(metricsUrl)), s"${host}-metrics-fetcher")

  def receive = {
    case Poll => metricsFetcher ! Fetch
    case Fetched(fetchResult) => {
      mongoWriter ! WriteToMongo(fetchResult)
    }
  }
}
