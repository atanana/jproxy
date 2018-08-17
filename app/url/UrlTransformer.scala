package url

import io.lemonlabs.uri.{AbsoluteUrl, Url}
import javax.inject.Singleton
import url.UrlTransformer.{KEY_HOST, KEY_SCHEME}

@Singleton
class UrlTransformer {
  type Params = Map[String, Seq[String]]

  def transformToExternalUrl(path: String, params: Params): Url = {
    val host = params(KEY_HOST).head
    val scheme = params(KEY_SCHEME).head
    Url(
      scheme = scheme,
      host = host,
      path = path,
    )
  }

  def transformToInternalUrl(urlStr: String): String = {
    val url = AbsoluteUrl.parse(urlStr)
    val resultUrl = url.addParam(KEY_HOST, url.host.toString())
      .addParam(KEY_SCHEME, url.scheme)
    resultUrl.path + resultUrl.query.toString()
  }
}

object UrlTransformer {
  def apply(): UrlTransformer = new UrlTransformer()

  val KEY_HOST = "host"

  val KEY_SCHEME = "scheme"
}
