package wrappers

import javax.inject.{Inject, Singleton}
import play.api.libs.ws.WSClient
import wrappers.WebWrapper.HEADER_AUTHORIZATION

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

@Singleton
class WebWrapper @Inject()(ws: WSClient) {
  def readUrl(url: String, auth: Option[String]): String = {
    val future = ws.url(url)
      .withHttpHeaders(headers(auth): _*)
      .get()
      .map(response => response.body)
    Await.result(future, 30.seconds)
  }

  private def headers(auth: Option[String]) = {
    auth.map(header => (HEADER_AUTHORIZATION, header)).toList
  }
}

object WebWrapper {
  val HEADER_AUTHORIZATION = "Authorization"
}