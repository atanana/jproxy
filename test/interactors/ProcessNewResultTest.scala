package interactors

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, OneInstancePerTest}
import url.UrlTransformer
import wrappers.{ConfigWrapper, FileSystemWrapper}

class ProcessNewResultTest extends FunSuite with MockFactory with OneInstancePerTest {
  private val fsWrapper = mock[FileSystemWrapper]
  private val configWrapper = stub[ConfigWrapper]
  private val transformer = stub[UrlTransformer]
  private val interactor = new ProcessNewResult(fsWrapper, configWrapper, transformer)

  private val cacheFile = "cache"
  private val testHost = "test.com"
  private val testPort = 9000
  private val testUrl = "https://www.nflgamepass.com/api/user/api/users/v1/profile/me"
  private val testUrlProcessed = "http://test.com/api/user/api/users/v1/profile/me?host=www.nflgamepass.com&scheme=https"

  configWrapper.host _ when() returns testHost
  configWrapper.port _ when() returns testPort
  transformer.transformToInternalUrl _ when(testUrl, testHost, testPort) returns testUrlProcessed

  test("should replace urls") {
    stubIgnoredPrefixes(List.empty)
    expectAnyCache
    val result = interactor.process(cacheFile, s"test $testUrl test")
    assert(result == s"test $testUrlProcessed test")
  }

  test("should replace multiple urls") {
    stubIgnoredPrefixes(List.empty)
    expectAnyCache
    val result = interactor.process(cacheFile, s"test $testUrl test $testUrl test")
    assert(result == s"test $testUrlProcessed test $testUrlProcessed test")
  }

  test("should not replace ignored urls") {
    stubIgnoredPrefixes(List("https://www.nflgamepass.com/api/user"))
    expectAnyCache
    val result = interactor.process(cacheFile, s"test $testUrl test")
    assert(result == s"test $testUrl test")
  }

  test("should store processed result") {
    stubIgnoredPrefixes(List.empty)
    fsWrapper.writeFile _ expects(cacheFile, s"test $testUrlProcessed test")
    interactor.process(cacheFile, s"test $testUrl test")
  }

  private def stubIgnoredPrefixes(prefixes: Seq[String]): Unit = {
    configWrapper.ignoredUrlPrefixes _ when() returns prefixes
  }

  private def expectAnyCache = {
    fsWrapper.writeFile _ expects(*, *)
  }
}
