package com

import com.typesafe.config.{Config, ConfigFactory}

package object tweet {
  private val conf: Config = ConfigFactory.load()

  val TWITTER_SECRETS: String = conf.getString("twitter.secrets_file")
  val TWITTER_API: String = conf.getString("twitter.api")

  val KAFKA_BOOTSTRAP_SERVER: String = conf.getString("kafka.bootstrap_server")
  val KAFKA_OFFSET_RESET: String = conf.getString("kafka.consumer_offset_reset")
}
