package com.rtjfarrimond.serialization

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.rtjfarrimond.database.DatabaseDelegate.ActionPerformed
import com.rtjfarrimond.domain.request.InterestedPartyCreateRequest
import com.rtjfarrimond.domain.response.InterestedPartyCreateResponse
import spray.json.{DeserializationException, JsString, JsValue, JsonFormat, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport {
  import spray.json.DefaultJsonProtocol._

  implicit object UUIDFormat extends JsonFormat[UUID] {
    def write(uuid: UUID) = JsString(uuid.toString)
    def read(value: JsValue): UUID = {
      value match {
        case JsString(uuid) => UUID.fromString(uuid)
        case _ => throw DeserializationException("Expected hexadecimal UUID string")
      }
    }
  }

  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed)

  implicit val interestedPartyCreateRequestJsonFormat: RootJsonFormat[InterestedPartyCreateRequest] = jsonFormat2(InterestedPartyCreateRequest)
  implicit val interestedPartyCreateResponseJsonFormat: RootJsonFormat[InterestedPartyCreateResponse] = jsonFormat3(InterestedPartyCreateResponse)

}
