package wrappers

import javax.inject.Inject
import play.api.Configuration

class ConfigWrapper @Inject()(config: Configuration) {
  def ignoredUrlPrefixes: Seq[String] = {
    config.get[Option[Seq[String]]]("ignoredUrls").getOrElse(List.empty)
  }
}
