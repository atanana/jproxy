package interactors

import akka.japi.Option.Some
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
  val testContent2 = "test content2"
  private val testAuth: Option[String] = Some("test")

  var transformer: UrlTransformer = _
  var fsWrapper: FileSystemWrapper = _
  var webWrapper: WebWrapper = _
  var processor: ProcessNewResult = _
  var interactor: ProxyInteractor = _

  override protected def beforeEach(): Unit = {
    transformer = stub[UrlTransformer]
    fsWrapper = stub[FileSystemWrapper]
    webWrapper = stub[WebWrapper]
    processor = stub[ProcessNewResult]
    interactor = new ProxyInteractor(transformer, fsWrapper, webWrapper, processor)
  }

  test("it should get result from cache") {
    prepareTransformer
    setFileExists(true)
    fsWrapper.readFile _ when s"store$testPath.json" returns testContent
    assert(interactor.processRequest(testPath, testParams, testAuth) == testContent)
  }

  test("it should get result from net") {
    prepareTransformer
    setFileExists(false)
    webWrapper.readUrl _ when(testUrl.toString(), testAuth) returns testContent
    processor.process _ when(s"store$testPath.json", testContent) returns testContent2
    assert(interactor.processRequest(testPath, testParams, testAuth) == testContent2)
  }

  private def prepareTransformer = {
    transformer.transformToExternalUrl _ when(testPath, testParams) returns testUrl
  }

  private def setFileExists(isExists: Boolean): Any = {
    fsWrapper.isFileExists _ when s"store$testPath.json" returns isExists
  }
}
