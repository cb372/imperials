package com.github.cb372.imperials.client

import com.typesafe.config.{ConfigException, Config}

import collection.JavaConverters._
import com.typesafe.config.ConfigException.Missing
import concurrent.duration.{FiniteDuration, Duration}

/**
 * Author: chris
 * Created: 3/3/13
 */
class Settings(config: Config) {

  val imperialsConfig = config.getConfig("imperials")

  def dashboardNotifyUrl: Option[String] = getString(imperialsConfig, "dashboardNotifyUrl")

  def mongoHost: String = getString(imperialsConfig, "mongo.host") getOrElse "localhost"
  def mongoPort: Int = getInt(imperialsConfig, "mongo.port") getOrElse 27017

  def hosts: Seq[String] = imperialsConfig.getStringList("hosts").asScala

  def metricsUrl(host: String): String = {
    val opt = for {
      hostConfig <- getConfig(imperialsConfig, host)
      metricsUrl <- getString(hostConfig, "metricsUrl")
    }  yield metricsUrl

    opt getOrElse { throw new Missing(s"imperials.${host}.metricsUrl") }
  }

  def pollInterval(host: String): FiniteDuration = {
    val specificValue = for {
      hostConfig <- getConfig(imperialsConfig, host)
      interval <- getDuration(hostConfig, "pollInterval")
    }  yield interval
    val defaultValue = for {
      hostConfig <- getConfig(imperialsConfig, "defaults")
      interval <- getDuration(hostConfig, "pollInterval")
    }  yield interval

    (specificValue orElse defaultValue) getOrElse { throw new Missing(s"imperials.${host}.pollInterval") }
  }

  private def getConfig(config: Config, path: String): Option[Config] = {
    safeGet(config, path) { (config, path) => config.getConfig(path) }
  }

  private def getString(config: Config, path: String): Option[String] = {
    safeGet(config, path) { (config, path) => config.getString(path) }
  }

  private def getInt(config: Config, path: String): Option[Int] = {
    safeGet(config, path) { (config, path) => config.getInt(path) }
  }

  private def getDuration(config: Config, path: String): Option[FiniteDuration] = {
    safeGet(config, path) { (config, path) => Duration.fromNanos(config.getNanoseconds(path)) }
  }

  private def safeGet[A](config: Config, path: String)(f: (Config, String) => A): Option[A] = {
    if (config.hasPath(path))
      Some(f(config, path))
    else
      None
  }
}
