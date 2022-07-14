package com.tweet_streaming
package stream

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.{Logger, LoggerFactory}

import java.util.Properties

class TestProducer {
  private val log: Logger = LoggerFactory.getLogger(classOf[TestProducer])

  def writeToKafka(topic: String): Unit = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVER)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)

    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, "ex-key", "ex_value")

    // asynchronous operation and won't wait till record sent to topic
    producer.send(record, new Callback {
      override def onCompletion(metadata: RecordMetadata, e: Exception): Unit = {
        // executed whenever record is sent successfully or when an exception is thrown
        if (e == null) {
          log.info(
            s"Topic: ${metadata.topic()}\n " +
              s"Key: ${record.key()}" +
              s"Partition: ${metadata.partition()}" +
              s"Offset: ${metadata.offset()}\n " +
              s"Timestamp: ${metadata.timestamp()}"
          )
        }
      }
    })

    // will flush and close - flush is synchronous and needed to prevent
    // application from closing before producer can send data to topic
    producer.close()
  }
}

object TestProducer {
  def main(args: Array[String]): Unit = {
    val test = new TestProducer()
    test.log.info("Started producer!")

    test.writeToKafka(topic = args(0))
  }
}
