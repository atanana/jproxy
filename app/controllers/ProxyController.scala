package controllers

import interactors.ProxyInteractor
import javax.inject.{Inject, Singleton}
import play.api.mvc._

@Singleton
class ProxyController @Inject()(cc: ControllerComponents, proxyInteractor: ProxyInteractor) extends AbstractController(cc) {
  def index(path: String) = Action { implicit request: Request[AnyContent] =>
    if (request.queryString.nonEmpty) {
      Ok(proxyInteractor.processRequest(request.path, request.queryString))
    } else {
      Ok
    }
  }

  def default = Action {
    Ok("Hello there!")
  }
}
