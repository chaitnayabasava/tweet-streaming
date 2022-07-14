package com.tweet_streaming.twitter

case class TwitterResponse(
                            data: Array[TwitterData],
                            meta: TwitterMeta
                          )

case class TwitterData(id: String, text: String)

case class TwitterMeta(newest_id: String, oldest_id: String, result_count: Double, next_token: String = "")
