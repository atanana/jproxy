package wrappers

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{FileSystems, Files}

import javax.inject.Singleton

import scala.io.Source

@Singleton
class FileSystemWrapper {
  def isFileExists(path: String): Boolean = new File(path).exists()

  def readFile(path: String): String = Source.fromFile(new File(path)).mkString

  def writeFile(path: String, contents: String): Unit = {
    val fullPath = FileSystems.getDefault.getPath(path).toAbsolutePath
    fullPath.getParent.toFile.mkdirs()
    Files.write(fullPath, contents.getBytes(StandardCharsets.UTF_8))
  }
}
