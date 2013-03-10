package com.github.cb372.imperials.client.actor

import org.joda.time.DateTime
import org.json4s._

/**
 * Author: chris
 * Created: 3/3/13
 */
object Messages {

  case class FetchResult(host: String, fetchTime: DateTime, data: JValue)

  case object Poll
  case object Fetch
  case class RewriteJson(fetchResult: FetchResult)
  case class Fetched(fetchResult: FetchResult)
  case class Rewrote(fetchResult: FetchResult)
  case class WriteToMongo(fetchResult: FetchResult)
  case class NotifyDashboard(fetchResult: FetchResult)

}

