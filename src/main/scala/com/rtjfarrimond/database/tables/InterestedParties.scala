package com.rtjfarrimond.database.tables

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

import slick.jdbc.H2Profile.api._

object InterestedParties extends TableQuery(new InterestedParties(_)) {
  val findByUuid = this.findBy(_.uuid)
  val findBySaxId = this.findBy(_.saxId)
  val findByName = this.findBy(_.name)
  val findByEmail = this.findBy(_.email)

  def createSchemaIfNotExists(): Unit = this.schema.createIfNotExists
}

class InterestedParties(tag: Tag) extends Table[(UUID, Option[String], String, String, String)](tag, "interested_parties") {
  def uuid: Rep[UUID] = column[UUID]("uuid", O.PrimaryKey)
  def createdDate: Rep[Timestamp] = column[Timestamp]("created_date")
  def modifiedDate: Rep[Timestamp] = column[Timestamp]("modified_date", O.Default[Timestamp](Timestamp.valueOf(LocalDateTime.now)))
  def saxId: Rep[Option[String]] = column[Option[String]]("sax_id", O.Unique)
  def name: Rep[String] = column[String]("name")
  def email: Rep[String] = column[String]("email")

  def *  = (uuid, saxId, name, email, s"$name $email")
}
