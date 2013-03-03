package com.github.cb372.imperials.client.actor

import com.mongodb.casbah.Imports._
import akka.actor.Actor
import akka.event.Logging
import com.github.cb372.imperials.client.actor.Messages.{FetchResult, WriteToMongo}
import com.mongodb.util.JSON
import com.mongodb.DBObject
import com.mongodb.casbah.commons.conversions.scala._

/**
 * Author: chris
 * Created: 3/3/13
 */
class MongoWriter(val mongo: MongoCollection) extends Actor {
  val log = Logging(context.system, this)

  RegisterJodaTimeConversionHelpers()

  def receive = {
    case WriteToMongo(FetchResult(fetchTime, json)) => {
      val data = JSON.parse(json).asInstanceOf[DBObject]
      val keysWithDots: collection.Map[String, AnyRef] = data.filterKeys(_.contains("."))
      keysWithDots.foreach { case (k, v) =>
        data.put(k.replaceAllLiterally(".", "_"), v)
        data.removeField(k)
      }
      val mongoObj = MongoDBObject(
        "fetchTime" -> fetchTime,
        "data" -> data
      )
      mongo.save(mongoObj)
      log.info("Saved metrics info to MongoDB")
    }
  }
}
