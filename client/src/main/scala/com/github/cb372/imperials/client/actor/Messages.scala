package com.github.cb372.imperials.client.actor

import org.joda.time.DateTime

/**
 * Author: chris
 * Created: 3/3/13
 */
object Messages {

  case class FetchResult(fetchTime: DateTime, json: String)

  case object Poll
  case object Fetch
  case class Fetched(fetchResult: FetchResult)
  case class WriteToMongo(fetchResult: FetchResult)
  case class NotifyDashboard(fetchResult: FetchResult)

}

