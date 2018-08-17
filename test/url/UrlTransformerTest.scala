package url

import io.lemonlabs.uri.Url
import org.scalatest.FunSuite

class UrlTransformerTest extends FunSuite {

  test("testTransformToExternalUrl") {
    val transformer = UrlTransformer()
    val params = Map(
      ("host", List("www.nflgamepass.com")),
      ("scheme", List("https"))
    )
    val result = transformer.transformToExternalUrl("/api/en/content/v5/mobile/config", params)
    assert(Url.parse("https://www.nflgamepass.com/api/en/content/v5/mobile/config") == result)
  }

}
