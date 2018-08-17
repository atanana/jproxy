package interactors

import javax.inject.{Inject, Singleton}
import url.UrlTransformer
import wrappers.FileSystemWrapper

@Singleton
class ProcessNewResult @Inject()(fsWrapper: FileSystemWrapper, transformer: UrlTransformer) {
  private val urlRegexp = """(https?://[\w\d\./{}\[\]-_&?#]*)""".r

  def process(cacheFile: String, result: String): String = {
    val processedResult = replaceUrls(result)
    cacheResult(cacheFile, processedResult)
    processedResult
  }

  private def replaceUrls(result: String): String =
    urlRegexp.replaceAllIn(result, m => transformer.transformToInternalUrl(m.toString()))


  private def cacheResult(cacheFile: String, processedResult: String): Unit = {
    fsWrapper.writeFile(cacheFile, processedResult)
  }
}
