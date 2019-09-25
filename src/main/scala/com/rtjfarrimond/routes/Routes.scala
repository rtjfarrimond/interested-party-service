package com.rtjfarrimond.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.rtjfarrimond.JsonSupport
import com.rtjfarrimond.domain.Greeting

import scala.concurrent.Future

trait Routes extends JsonSupport {

  lazy val helloRoutes: Route = pathPrefix("hello") {
    concat(helloWorldRoute, helloNameRoute)
  }

  private lazy val helloWorldRoute: Route =
    pathEnd {
      concat(get(greet("World")))
    }

  private lazy val helloNameRoute: Route =
    path(Segment) {
      name => concat(get(greet(name)))
  }

  private def greet(recipient: String): Route = {
    rejectEmptyResponse {
      complete(
        Future.successful(
          Some(Greeting(s"Hello $recipient!"))))
    }
  }


}
