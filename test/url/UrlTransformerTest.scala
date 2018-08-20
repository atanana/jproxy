package url

import io.lemonlabs.uri.Url
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import url.UrlTransformer.{KEY_HOST, KEY_SCHEME}

class UrlTransformerTest extends FunSuite with BeforeAndAfterEach {

  var transformer: UrlTransformer = _

  override protected def beforeEach(): Unit = {
    transformer = UrlTransformer()
  }

  test("testTransformToExternalUrl") {
    val params = Map(
      (KEY_HOST, List("www.nflgamepass.com")),
      (KEY_SCHEME, List("https"))
    )
    val result = transformer.transformToExternalUrl("/api/en/content/v5/mobile/config", params)
    assert(result == Url.parse("https://www.nflgamepass.com/api/en/content/v5/mobile/config"))
  }

  test("testTransformToInternalUrl") {
    val result = transformer.transformToInternalUrl("https://www.nflgamepass.com/api/en/content/v5/mobile/teams/{u.preferredTeam}/details")
    assert(result == "/api/en/content/v5/mobile/teams/{u.preferredTeam}/details?host=www.nflgamepass.com&scheme=https")
  }
}
