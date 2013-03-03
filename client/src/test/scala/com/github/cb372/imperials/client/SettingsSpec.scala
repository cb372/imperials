package com.github.cb372.imperials.client

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import com.typesafe.config.{ConfigException, ConfigFactory}
import concurrent.duration._

/**
 * Author: chris
 * Created: 3/3/13
 */
class SettingsSpec extends FlatSpec with ShouldMatchers {

  val configFile =
    """
      |imperials {
      |    defaults {
      |        pollInterval = 10m
      |    }
      |
      |    hosts = [ "host1", "host2", "host3", "host4" ]
      |
      |    host1 {
      |        metricsUrl = "http://host1/metrics"
      |        pollInterval = 1m
      |    }
      |    host2 {
      |        metricsUrl = "http://host2/metrics"
      |    }
      |    host3 {
      |    }
      |}
    """.stripMargin

  val settings = new Settings(ConfigFactory.parseString(configFile))

  behavior of "Settings"

  it should "return the list of hosts" in {
    settings.hosts.toList should be(List("host1", "host2", "host3", "host4"))
  }

  it should "return the metricsUrl for a given host" in {
    settings.metricsUrl("host1") should be("http://host1/metrics")
  }

  it should "throw an exception if metricsUrl is not set for a host" in {
    intercept[ConfigException.Missing] {
     settings.metricsUrl("host3")
    }
  }

  it should "throw an exception if metricsUrl is not set for a host (host has no config section)" in {
    intercept[ConfigException.Missing] {
      settings.metricsUrl("host4")
    }
  }

  it should "return the specified pollInterval for a given host" in {
    settings.pollInterval("host1") should be(1.minutes)
  }

  it should "return the default pollInterval if it is not specified for the given host" in {
    settings.pollInterval("host2") should be(10.minutes)
  }

}
