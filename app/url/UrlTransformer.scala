package url

import io.lemonlabs.uri.Url
import javax.inject.Singleton

@Singleton
class UrlTransformer {
  type Params = Map[String, Seq[String]]

  def transformToExternalUrl(path: String, params: Params): Url = {
    val host = params("host").head
    val scheme = params("scheme").head
    Url(
      scheme = scheme,
      host = host,
      path = path,
    )
  }
}

object UrlTransformer {
  def apply(): UrlTransformer = new UrlTransformer()
}
