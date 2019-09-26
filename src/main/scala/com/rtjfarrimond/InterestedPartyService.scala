package com.rtjfarrimond

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.rtjfarrimond.database.{DatabaseDelegate, PostgresDelegate}
import com.rtjfarrimond.routes.InterestedPartyRoutes

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

object InterestedPartyService extends App with InterestedPartyRoutes {
  private implicit val actorSystem: ActorSystem = ActorSystem("interestedPartySystem")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContext = actorSystem.dispatcher

  lazy val routes: Route = interestedPartyRoutes

  // TODO: Get db params from configuration
  private val user: String = "postgres"
  private val pass: String = "example"
  private val server: String = "localhost"
  private val databaseName: String = "postgres"
  private val port: Int = 5432

  override val database: DatabaseDelegate = PostgresDelegate(
  user,
  pass,
  server,
  databaseName,
  port)

  // TODO: Move server and port to config
  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "localhost", 8080)
  serverBinding.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      Console.err.println(s"Server could not start!")
      e.printStackTrace()
      actorSystem.terminate()
  }

  Await.result(actorSystem.whenTerminated, Duration.Inf)
  database.close()
  println("Application exiting...")

}
