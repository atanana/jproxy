package url

import io.lemonlabs.uri.Url
import org.scalatest.FunSuite

class UrlTransformerTest extends FunSuite {

  test("testTransformToExternalUrl") {
    val transformer = UrlTransformer()
    val result = transformer.transformToExternalUrl("api/en/content/v5/mobile/config?host=www.nflgamepass.com")
    assert(Url(host = "www.nflgamepass.com", path = "api/en/content/v5/mobile/config") == result)
  }

}
