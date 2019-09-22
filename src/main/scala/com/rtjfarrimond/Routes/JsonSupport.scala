package com.rtjfarrimond.Routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

trait JsonSupport extends SprayJsonSupport {
  import spray.json.DefaultJsonProtocol._

  implicit val helloJsonFormat = jsonFormat1(Greeting)

}
