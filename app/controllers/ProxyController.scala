package controllers

import interactors.ProxyInteractor
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class ProxyController @Inject()(cc: ControllerComponents, proxyInteractor: ProxyInteractor) extends AbstractController(cc) {
  def index(path: String) = Action { implicit request: Request[AnyContent] =>
    Ok(proxyInteractor.processRequest(request.path, request.queryString))
  }
}
