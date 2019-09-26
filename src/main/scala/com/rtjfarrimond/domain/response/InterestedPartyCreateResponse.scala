package com.rtjfarrimond.domain.response

import java.util.UUID

final case class InterestedPartyCreateResponse(uuid: UUID, name: String, email: String) {
  override def toString: String = s"{\n  uuid: $uuid\n  name: $name,\n  email: $email\n}"
}
