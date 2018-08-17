package interactors

import java.net.URL

import io.lemonlabs.uri.Url
import javax.inject.{Inject, Singleton}
import url.UrlTransformer
import wrappers.{FileSystemWrapper, WebWrapper}

@Singleton
class ProxyInteractor @Inject()(
                                 urlTransformer: UrlTransformer,
                                 fsWrapper: FileSystemWrapper,
                                 webWrapper: WebWrapper,
                                 processor: ProcessNewResult
                               ) {
  def processRequest(path: String, params: Map[String, Seq[String]]): String = {
    val url = urlTransformer.transformToExternalUrl(path, params)
    val cacheFile = s"store${url.path.toString()}.json"
    if (isInCache(cacheFile)) {
      getResultFromCache(cacheFile)
    } else {
      getResultFromNet(url, cacheFile)
    }
  }

  private def isInCache(cacheFile: String) = {
    fsWrapper.isFileExists(cacheFile)
  }

  private def getResultFromCache(cacheFile: String) = {
    fsWrapper.readFile(cacheFile)
  }

  private def getResultFromNet(url: Url, cacheFile: String) = {
    val rawResult = webWrapper.readUrl(new URL(url.toString()))
    processor.process(cacheFile, rawResult)
  }
}
