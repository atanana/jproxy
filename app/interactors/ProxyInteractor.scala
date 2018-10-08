package interactors

import io.lemonlabs.uri.Url
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import url.UrlTransformer
import wrappers.{FileSystemWrapper, WebWrapper}

import scala.util.Try

@Singleton
class ProxyInteractor @Inject()(
                                 urlTransformer: UrlTransformer,
                                 fsWrapper: FileSystemWrapper,
                                 webWrapper: WebWrapper,
                                 processor: ProcessNewResult
                               ) {
  def processRequest(path: String, params: Map[String, Seq[String]], auth: Option[String]): String = {
    val url = urlTransformer.transformToExternalUrl(path, params)
    val cacheFile = s"store${url.path.toString()}.json"
    if (isInCache(cacheFile)) {
      getResultFromCache(cacheFile)
    } else {
      getResultFromNet(url, cacheFile, auth)
    }
  }

  private def isInCache(cacheFile: String) = {
    fsWrapper.isFileExists(cacheFile)
  }

  private def getResultFromCache(cacheFile: String) = {
    fsWrapper.readFile(cacheFile)
  }

  private def getResultFromNet(url: Url, cacheFile: String, auth: Option[String]) = {
    val rawResult = webWrapper.readUrl(url.toString(), auth)
    processor.process(cacheFile, prettify(rawResult))
  }

  private def prettify(contents: String): String =
    Try {
      val json = Json.parse(contents)
      Json.prettyPrint(json)
    }.getOrElse(contents)
}
