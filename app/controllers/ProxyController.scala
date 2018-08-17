package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class ProxyController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def index(path: String) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
