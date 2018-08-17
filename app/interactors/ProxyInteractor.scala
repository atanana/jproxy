package interactors

import java.net.URL

import javax.inject.{Inject, Singleton}
import url.UrlTransformer
import wrappers.{FileSystemWrapper, WebWrapper}

@Singleton
class ProxyInteractor @Inject()(urlTransformer: UrlTransformer, fsWrapper: FileSystemWrapper, webWrapper: WebWrapper) {
  def processRequest(path: String, params: Map[String, Seq[String]]): String = {
    val url = urlTransformer.transformToExternalUrl(path, params)
    val cacheFile = s"store${url.path.toString()}.json"
    if (fsWrapper.isFileExists(cacheFile)) {
      fsWrapper.readFile(cacheFile)
    } else {
      val result = webWrapper.readUrl(new URL(url.toString()))
      fsWrapper.writeFile(cacheFile, result)
      result
    }
  }
}
