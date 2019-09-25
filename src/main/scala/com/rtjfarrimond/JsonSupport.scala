package com.rtjfarrimond

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.rtjfarrimond.domain.Greeting

trait JsonSupport extends SprayJsonSupport {
  import spray.json.DefaultJsonProtocol._

  implicit val helloJsonFormat = jsonFormat1(Greeting)

}
