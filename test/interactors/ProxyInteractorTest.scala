package interactors

import java.net.URL

import io.lemonlabs.uri.Url
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import url.UrlTransformer
import url.UrlTransformer.{KEY_HOST, KEY_SCHEME}
import wrappers.{FileSystemWrapper, WebWrapper}

class ProxyInteractorTest extends FunSuite with BeforeAndAfterEach with MockFactory {
  val testPath = "/test/path"
  val testParams = Map(
    (KEY_HOST, List("test host")),
    (KEY_SCHEME, List("http"))
  )
  private val testUrl = Url.parse(s"http://test.com$testPath")
  val testContent = "test content"

  var transformer: UrlTransformer = _
  var fsWrapper: FileSystemWrapper = _
  var webWrapper: WebWrapper = _
  var interactor: ProxyInteractor = _

  override protected def beforeEach(): Unit = {
    transformer = stub[UrlTransformer]
    fsWrapper = mock[FileSystemWrapper]
    webWrapper = stub[WebWrapper]
    interactor = new ProxyInteractor(transformer, fsWrapper, webWrapper)
  }

  test("it should get result from cache") {
    prepareTransformer
    setFileExists(true)
    fsWrapper.readFile _ expects s"store$testPath.json" returns testContent
    checkResult
  }

  test("it should get result from net") {
    prepareTransformer
    setFileExists(false)
    webWrapper.readUrl _ when new URL(testUrl.toString()) returns testContent
    fsWrapper.writeFile _ expects(*, *)
    checkResult
  }

  test("it should save result to cache") {
    prepareTransformer
    setFileExists(false)
    webWrapper.readUrl _ when new URL(testUrl.toString()) returns testContent
    fsWrapper.writeFile _ expects(s"store$testPath.json", testContent)
    checkResult
  }

  private def prepareTransformer = {
    transformer.transformToExternalUrl _ when(testPath, testParams) returns testUrl
  }

  private def checkResult = {
    assert(interactor.processRequest(testPath, testParams) == testContent)
  }

  private def setFileExists(isExists: Boolean): Any = {
    fsWrapper.isFileExists _ expects s"store$testPath.json" returns isExists
  }
}
