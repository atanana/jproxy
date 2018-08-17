package wrappers

import java.io.File

import javax.inject.Singleton

import scala.io.Source

@Singleton
class FileSystemWrapper {
  def isFileExists(path: String): Boolean = new File(path).exists()

  def readFile(path: String): String = Source.fromFile(new File(path)).mkString
}
