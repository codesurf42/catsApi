package com.cats

import java.net.URI
import java.nio.file.{Files, Paths}

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import play.api.libs.json._

import scala.xml._

object ApiClient {
  def main(args: Array[String]): Unit = {

    val firstArg = args.toList.headOption

    println("Started with parameter: " + firstArg)

    firstArg match {
      case Some("categories") => Categories.run()
      case Some("fact") => Fact.run()
      case _ => CatImage.run()
    }
  }
}

trait Links {
  def getContentFromUrl(url: String):String = io.Source.fromURL(url).mkString

  def getBinaryContentFromUrl(uri: URI) = io.Source.fromURL(uri.toURL)(scala.io.Codec.ISO8859).map(_.toByte).toArray

  /** get the file/uri image extension if it looks like having such a file name */
  def getImageExtension(uri: URI):Option[String] = {
    val reg = "\\.(png|jpg|gif)$".r("ext")
    reg.findFirstMatchIn(uri.getPath.toLowerCase).map(_.group("ext"))
  }
}

abstract class Cats(val contentUrl: String) {
  val logger = Logger(LoggerFactory.getLogger("Cats"))

  def run():Unit

  type CatFact = String
  case class Category(id: Int, name: String)
}

object Categories extends Cats(contentUrl = "http://thecatapi.com/api/categories/list") with Links {

  /**
    * Print to stdout an ordered list with the different categories of cats
    *
    * @return
    */
  override def run() = {
    println("run " + contentUrl)
    logger.info("Get content: " + contentUrl)
    val xml = getContentFromUrl(contentUrl)
    logger.info("Got content: " + xml)
    val items = parseXml(xml)
    showItems(items)
  }

  def parseXml(s:String):Iterable[Category] = {
    val xml = XML.loadString(s)

    (xml \\ "category").map { node =>
      Category(node.\("id").text.toInt, node.\("name").text)
    }
  }

  def showItems(items: Iterable[Category]):Unit = {
    println("Categories")
    // if we need to sort it in a different order - change here, e.g.
    // items.toSeq.sortBy(_.name) foreach( categ =>
    items.foreach(categ =>
      println(s"Category: ${categ.name} id: ${categ.id}")
    )
  }
}

object Fact extends Cats(contentUrl = "http://catfacts-api.appspot.com/api/facts?number=1") with Links {
  /**
    * Print to stdout a cat fact
    *
    * @return
    */
  override def run() = {
    val content = getContentFromUrl(contentUrl)
    val fact = parseJson(content)

    println("Fact about a cat: " + fact)
  }

  def parseJson(jsonString:String):CatFact = {
    Json.parse(jsonString).\("facts").head.as[String]
  }

}
object CatImage extends Cats(contentUrl = "http://thecatapi.com/api/images/get?format=xml") with Links {
  /**
    * Save (on the temporary folder) an image of a cat (as an image
    * file) and print to stdout the url
    *
    * @return
    */
  override def run() = {
    val contentXml = getContentFromUrl(contentUrl)

    // the only problem with such nice-looking for-comprehensions are their not-so-nice-debuggable behaviour
    for {
      uri <- parse(contentXml)
      _ = logger.debug("Image URL: " + uri)
      ext <- getImageExtension(uri)
    } yield {
      val path = imageToFile(getBinaryContentFromUrl(uri), ext)
      logger.debug("Image path: " + path)
      println("Image URL: " + uri)
    }
  }

  def imageToFile(content: Array[Byte], ext: String = "image"):URI = {
    val path = java.io.File.createTempFile("cats_", "." + ext).toURI

    logger.debug(s"Saving to file: [$path]")
    logger.debug("Content length: " + content.size)
    Files.write(Paths.get(path), content)
    path
  }

  def parse(xmlString: String):Option[URI] = {
    val xml = XML.loadString(xmlString)
    xml.\\("url").headOption.map(n => URI.create(n.text))
  }
}
