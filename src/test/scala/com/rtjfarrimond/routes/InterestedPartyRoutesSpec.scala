package com.rtjfarrimond.routes

import java.util.UUID

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ContentTypes, MessageEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.rtjfarrimond.database.DatabaseDelegate.ActionPerformed
import com.rtjfarrimond.database.tables.InterestedPartyTable
import com.rtjfarrimond.database.{DatabaseDelegate, PostgresDelegate}
import com.rtjfarrimond.domain.request.InterestedPartyCreateRequest
import com.rtjfarrimond.domain.response.InterestedPartyCreateResponse
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import slick.dbio.DBIO
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

class InterestedPartyRoutesSpec extends WordSpec with Matchers with MockFactory with ScalaFutures
  with ScalatestRouteTest with InterestedPartyRoutes {

  override val database: DatabaseDelegate = stub[PostgresDelegate]

  val seq: DBIO[Unit] = DBIO.seq(
    InterestedPartyTable.schema.createIfNotExists,
    InterestedPartyTable += (UUID.randomUUID(), None, "expectedName", "expectedEmail")
  )

  "InterestedPartyRoutes" should {
    "be able to add interested party (POST /interested-party)" in {
      val expectedAp: ActionPerformed = ActionPerformed("success")
      val expectedApFuture: Future[ActionPerformed] = Future(expectedAp)
      (database.run _).when(*, *, *).returning(expectedApFuture)

      val expectedName: String = "Robert Farrimond"
      val expectedEmail: String = "robert.farrimond@ktech.com"
      val ip: InterestedPartyCreateRequest = InterestedPartyCreateRequest(expectedName, expectedEmail)
      val ipEntity = Marshal(ip).to[MessageEntity].futureValue

      val request = Post("/interested-party").withEntity(ipEntity)

      request ~> interestedPartyRoutes ~> check {
        status should === (StatusCodes.Created)
        contentType should === (ContentTypes.`application/json`)
        
        val responseObject: InterestedPartyCreateResponse = entityAs[InterestedPartyCreateResponse]
        responseObject.email should === ("robert.farrimond@ktech.com")
        responseObject.name should === ("Robert Farrimond")
        responseObject.uuid should not be (null)
      }
    }
  }
}
