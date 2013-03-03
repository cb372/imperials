package com.github.cb372.imperials.client

import actor.Messages.Poll
import actor.MetricsPoller
import com.typesafe.config.ConfigFactory

import com.mongodb.casbah.Imports._
import akka.actor.{Props, ActorSystem}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._

/**
 * Author: chris
 * Created: 3/3/13
 */
object Client extends App {

  val settings = new Settings(ConfigFactory.load())

  val mongoClient = MongoClient(settings.mongoHost, settings.mongoPort)
  val mongoDb = mongoClient("imperials")

  val actorSystem = ActorSystem("imperials-client")

  for (host <- settings.hosts) {
    val metricsUrl = settings.metricsUrl(host)
    val mongoCollection = mongoDb(host)
    val hostActor = actorSystem.actorOf(Props(new MetricsPoller(host, metricsUrl, mongoCollection)), name = host)

    val pollInterval = settings.pollInterval(host)
    actorSystem.scheduler.schedule(0.seconds, pollInterval, hostActor, Poll)
  }

}
