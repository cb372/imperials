package com.github.cb372.imperials.client.actor

import akka.actor.Actor
import akka.event.Logging
import com.mongodb.casbah.commons.conversions.scala._
import org.json4s._
import com.github.cb372.imperials.client.actor.Messages.RewriteJson
import com.github.cb372.imperials.client.actor.Messages.FetchResult
import com.github.cb372.imperials.client.actor.Messages.Rewrote

/**
 * Author: chris
 * Created: 3/3/13
 */
class JsonRewriter extends Actor {
  val log = Logging(context.system, this)

  RegisterJodaTimeConversionHelpers()

  def receive = {
    case RewriteJson(FetchResult(host, fetchTime, json)) => {
      val fixedJson = rewriteJson(json)
      context.parent ! Rewrote(FetchResult(host, fetchTime, fixedJson))
    }
  }

  def replaceDots(key: String): String = key.replaceAllLiterally(".", "_")

  def rewriteJson(value: JValue): JValue = value match {
    case JArray(arr) => JArray(arr map rewriteJson)
    case JObject(fields) => JObject(fields map { case (k, v) => replaceDots(k) -> rewriteJson(v) })
    case j => j
  }

}
