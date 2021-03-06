package com.tweet_streaming
package twitter

import requests.Response
import ujson.Value.Value
import upickle.default

import scala.io.Source

class TwitterAPI {
  var tokens: Value = parseSecrets()

  implicit val dataRW: default.ReadWriter[TwitterData] = upickle.default.macroRW[TwitterData]
  implicit val metaRW: default.ReadWriter[TwitterMeta] = upickle.default.macroRW[TwitterMeta]
  implicit val responseRW: default.ReadWriter[TwitterResponse] = upickle.default.macroRW[TwitterResponse]

  def getTweets(next_token: String = "", from: String = "elonmusk"): TwitterResponse = {
    val uri = s"$TWITTER_API?query=from:$from" + (if (next_token != "") s"&next_token=$next_token" else "")

    val request: Response = requests.get(
      uri, headers = Map("Authorization" -> s"Bearer ${this.tokens("bearer_token").str}")
    )
    val json = upickle.default.read[TwitterResponse](request.text)
    json
  }

  def parseSecrets(): Value = {
    val apiContent = Source.fromResource(TWITTER_SECRETS).mkString
    ujson.read(apiContent)
  }

}

object TwitterAPI {
  def main(args: Array[String]): Unit = {
    val twitterAPI = new TwitterAPI()
    println(twitterAPI.getTweets().data.length)
  }
}
