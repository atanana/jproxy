package wrappers

import java.net.URL

import javax.inject.Singleton

import scala.io.Source

@Singleton
class WebWrapper {
  def readUrl(url: URL): String = Source.fromURL(url).mkString
}
