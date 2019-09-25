package com.rtjfarrimond

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.rtjfarrimond.database.tables.InterestedParties
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object InterestedPartiesService extends App with JsonSupport {
  private implicit val actorSystem: ActorSystem = ActorSystem("helloSystem")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContext = actorSystem.dispatcher

  // TODO: Get these from env vars
  private val user: String = "postgres"
  private val password: String = "example"
  private val database = Database.forURL("jdbc:postgresql://localhost:5432/postgres", user, password)

  try {
    println("Creating InterestedParties table...")
    val createInterestedParties: DBIO[Unit] = DBIO.seq(InterestedParties.schema.createIfNotExists)
    val createInterestedPartiesFuture: Future[Unit] = database.run(createInterestedParties)
    Await.result(createInterestedPartiesFuture, Duration.Inf)
  } finally {
    println("Closing database connection...")
    database.close()
  }

  println("Application exiting...")
  System.exit(0)
}
