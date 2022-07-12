package com.tweet.stream

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties

object TestProducer {
  def main(args: Array[String]): Unit = {
    writeToKafka(topic = args(0), bootstrap_server = "localhost:9092")
  }

  def writeToKafka(topic: String, bootstrap_server: String): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", bootstrap_server)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, "ex-key", "ex_value")

    producer.send(record)
    producer.close()
  }
}
