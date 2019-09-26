package com.rtjfarrimond.database

import com.rtjfarrimond.database.DatabaseDelegate.ActionPerformed
import slick.dbio.DBIO

import scala.concurrent.Future

object DatabaseDelegate {
  case class ActionPerformed(description: String)
}

trait DatabaseDelegate {
  def run(dbio: DBIO[Unit], successMessage: String, failMessage: String): Future[ActionPerformed]
  def close(): Unit
}
