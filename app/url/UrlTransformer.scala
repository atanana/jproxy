package url

import io.lemonlabs.uri.Url

class UrlTransformer {
  def transformToExternalUrl(urlStr: String): Url = {
    val url = Url.parse(urlStr)
    val host = url.query.paramMap("host").head
    Url(
      host = host,
      path = url.path.toString()
    )
  }
}

object UrlTransformer {
  def apply(): UrlTransformer = new UrlTransformer()
}
