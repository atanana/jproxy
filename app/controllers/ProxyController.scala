package controllers

import interactors.ProxyInteractor
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.mvc._
import wrappers.WebWrapper.HEADER_AUTHORIZATION

@Singleton
class ProxyController @Inject()(cc: ControllerComponents, proxyInteractor: ProxyInteractor) extends AbstractController(cc) {
  def index(path: String) = Action { implicit request: Request[AnyContent] =>
    if (request.queryString.nonEmpty) {
      Logger.debug(s"Handle ${request.path} ${request.queryString}")
      val auth = request.headers.get(HEADER_AUTHORIZATION)
      Ok(proxyInteractor.processRequest(request.path, request.queryString, auth))
    } else {
      Ok
    }
  }

  def default = Action {
    Ok("Hello there!")
  }
}
