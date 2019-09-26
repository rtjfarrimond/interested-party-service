package com.rtjfarrimond.database

import com.rtjfarrimond.database.DatabaseDelegate.ActionPerformed
import slick.dbio.DBIO
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

object PostgresDelegate {
  def apply(user: String, pass: String, server: String, database: String, port: Int): DatabaseDelegate =
    new PostgresDelegate(user, pass, server, database, port)
}

class PostgresDelegate(val user: String, val pass: String, val server: String, val database: String, val port: Int)
  extends DatabaseDelegate {

  private[this] val db = Database.forURL(
    s"jdbc:postgresql://$server:$port/$database",
    user,
    pass,
    driver="org.postgresql.Driver")

  override def run(dbio: DBIO[Unit], successMessage: String, failMessage: String): Future[ActionPerformed] = {
    val runFuture: Future[Unit] = db.run(dbio)
    runFuture.transform {
      case Success(_) => Try(ActionPerformed(successMessage))
      case Failure(e) =>
        println(s"$failMessage: ${e.getMessage}") // TODO: Proper logging
        Try(ActionPerformed(s"$failMessage"))
    }
  }

  override def close(): Unit = db.close()
}