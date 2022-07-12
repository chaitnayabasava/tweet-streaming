package com.tweet.stream

import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration
import java.util.Properties
import java.util.regex.Pattern

import scala.jdk.CollectionConverters._

object TestConsumer {
  def main(args: Array[String]): Unit = {
    consumeFromKafka(topic = args(0), bootstrap_server = "localhost:9092")
  }

  def consumeFromKafka(topic: String, bootstrap_server: String): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", bootstrap_server)
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "earliest")
    props.put("group.id", "consumer-group")

    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(Pattern.compile(topic))

    while (true) {
      val records = consumer.poll(Duration.ofMillis(1000)).asScala
      for (record <- records.iterator)
        println(s"value: ${record.value()} from ${record.topic()} topic -> offset: ${record.offset()}")
    }
  }
}
