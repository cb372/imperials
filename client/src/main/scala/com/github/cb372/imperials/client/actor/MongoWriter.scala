package com.github.cb372.imperials.client.actor

import com.mongodb.casbah.Imports._
import akka.actor.Actor
import akka.event.Logging
import com.github.cb372.imperials.client.actor.Messages.{FetchResult, WriteToMongo}
import com.mongodb.casbah.commons.conversions.scala._
import org.json4s.mongo.JObjectParser.parse

/**
 * Author: chris
 * Created: 3/3/13
 */
class MongoWriter(mongo: MongoCollection) extends Actor {
  val log = Logging(context.system, this)

  implicit val formats = org.json4s.DefaultFormats ++ org.json4s.ext.JodaTimeSerializers.all

  def receive = {
    case WriteToMongo(FetchResult(host, fetchTime, data)) => {
      val mongoObj = MongoDBObject(
        "host" -> host,
        "fetchTime" -> fetchTime,
        "data" -> parse(data)
      )
      mongo.save(mongoObj)
      log.info("Saved metrics info to MongoDB")
    }
  }
}
