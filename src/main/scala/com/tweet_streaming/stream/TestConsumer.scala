package com.tweet_streaming
package stream

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.errors.WakeupException
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.{Logger, LoggerFactory}

import java.time.Duration
import java.util.Properties
import java.util.regex.Pattern
import scala.jdk.CollectionConverters._

class TestConsumer {
  private val log: Logger = LoggerFactory.getLogger(classOf[TestConsumer])

  def consumeFromKafka(topic: String): Unit = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVER)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KAFKA_OFFSET_RESET)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group")

    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(Pattern.compile(topic))

    // get a reference to current thread - shutdown hook runs in diff thread
    val mainThread: Thread = Thread.currentThread()

    // shutdown hook
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        log.info("shutdown initialized")
        // when invoked, the next time poll is called it will throw WakeupException and exit the loop
        // needs to be called in a separate thread from which the consumer is running
        consumer.wakeup()

        // join main thread to allow execution of main thread
        try mainThread.join()
        catch {
          case e: InterruptedException => e.printStackTrace()
        }
      }
    })

    try {
      while (true) {
        // if no records, then consumer waits for specified time
        // if records are found, consumer will read as many records as possible before moving to next line
        val records = consumer.poll(Duration.ofMillis(1000)).asScala
        for (record <- records.iterator)
          log.info(s"value: ${record.value} from ${record.topic} topic & ${record.partition} -> offset: ${record.offset}")
      }
    } catch {
      case WakeupException => log.info("Wake up called")
      case e: Exception => e.printStackTrace()
    } finally {
      // is needed whenever consumer is shutdown or some exception occurred in code
      // will notify consumer group for proper re-balance
      consumer.close()
      log.info("consumer shutdown gracefully")
    }

  }
}

object TestConsumer {
  def main(args: Array[String]): Unit = {
    val testConsumer = new TestConsumer
    testConsumer.log.info("Started consumer!")

    testConsumer.consumeFromKafka(topic = args(0))
  }
}
