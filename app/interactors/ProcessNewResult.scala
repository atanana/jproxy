package interactors

import javax.inject.{Inject, Singleton}
import url.UrlTransformer
import wrappers.{ConfigWrapper, FileSystemWrapper}

@Singleton
class ProcessNewResult @Inject()(
                                  fsWrapper: FileSystemWrapper,
                                  configWrapper: ConfigWrapper,
                                  transformer: UrlTransformer
                                ) {
  private val urlRegexp = """(https?://[\w\d\./\{\}\[\]\-_&\?#,]*)""".r

  def process(cacheFile: String, result: String): String = {
    val processedResult = replaceUrls(result)
    cacheResult(cacheFile, processedResult)
    processedResult
  }

  private def replaceUrls(result: String): String =
    urlRegexp.replaceAllIn(result, m => transformUrl(m.toString()))

  private def transformUrl(url: String): String = {
    if (isIgnored(url)) {
      url
    } else {
      transformer.transformToInternalUrl(url, configWrapper.host, configWrapper.port)
    }
  }

  private def isIgnored(url: String): Boolean = {
    configWrapper.ignoredUrlPrefixes.exists(prefix => url.startsWith(prefix))
  }

  private def cacheResult(cacheFile: String, processedResult: String): Unit = {
    fsWrapper.writeFile(cacheFile, processedResult)
  }
}
