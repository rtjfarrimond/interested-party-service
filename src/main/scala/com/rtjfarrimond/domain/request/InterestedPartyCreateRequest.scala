package com.rtjfarrimond.domain.request

final case class InterestedPartyCreateRequest(name: String, email: String) {
  override def toString: String = s"{\n  name: $name,\n  email: $email\n}"
}
