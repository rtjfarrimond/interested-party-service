package com.rtjfarrimond.routes

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.rtjfarrimond.database.DatabaseDelegate
import com.rtjfarrimond.database.tables.InterestedPartyTable
import com.rtjfarrimond.domain.request.InterestedPartyCreateRequest
import com.rtjfarrimond.domain.response.InterestedPartyCreateResponse
import com.rtjfarrimond.serialization.JsonSupport
import com.typesafe.scalalogging.Logger
import slick.dbio.DBIO
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait InterestedPartyRoutes extends JsonSupport {

  val database: DatabaseDelegate

  lazy val interestedPartyRoutes: Route = pathPrefix("interested-party") {
    concat(createInterestedParty)
  }

  val logger = Logger("InterestedPartyRoutes")

  private def createInterestedParty: Route = post {
    val uuid: UUID = UUID.randomUUID
    entity(as[InterestedPartyCreateRequest]) { ip =>
      val seq: DBIO[Unit] = DBIO.seq(
        InterestedPartyTable.schema.createIfNotExists,
        InterestedPartyTable += (uuid, None, ip.name, ip.email)
      )
      val ipCreated: Future[InterestedPartyCreateResponse] = database.run(
        seq,
        s"Created interested party",
        s"Failed to create interested party")
          .transform {
            case Success(_) => Try(InterestedPartyCreateResponse(uuid, ip.name, ip.email))
            case Failure(e) => throw e
          }
      onSuccess(ipCreated) { created =>
        logger.info(s"Interested party created: ${ipCreated.value.get.get.toString}")
        complete((StatusCodes.Created, created))
      }
    }
  }

}
