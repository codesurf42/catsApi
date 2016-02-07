package com.cats

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

object ApiClient {
  def main(args: Array[String]): Unit = {

    val firstArg = args.toList.headOption

    println("Started with parameter: " + firstArg)

    firstArg match {
      case Some("categories") => Categories.run
      case Some("fact") => Fact.run
      case _ => CatImage.run
    }
  }
}

trait Links {
  def getContentFromUrl(url: String):String = io.Source.fromURL(url).mkString
}

abstract class Cats(val contentUrl: String) {
  val logger = Logger(LoggerFactory.getLogger("Cats"))

  type CatFact = String
  case class Category(id: Int, name: String)
}

object Categories extends Cats(contentUrl = "http://thecatapi.com/api/categories/list") with Links {

  /**
    * Print to stdout an ordered list with the different categories of cats
    *
    * @return
    */
  def run = {
    println("run " + contentUrl)
    logger.info("Get content: " + contentUrl)
    val xml = getContentFromUrl(contentUrl)
    logger.info("Got content: " + xml)
    val items = parseXml(xml)
    showItems(items)
  }

  import scala.xml._
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

object Fact extends Cats("http://catfacts-api.appspot.com/api/facts?number=1") with Links {
  /**
    * Print to stdout a cat fact
    *
    * @return
    */
  def run = ???

  def parseJson(jsonString:String):CatFact = {
    ???
  }

}
object CatImage {
  /**
    * Save (on the temporary folder) an image of a cat (as an image
    * file) and print to stdout the url
    *
    * @return
    */
  def run = ???
  def imageToFile = ???
  // http://thecatapi.com/api/images/get?format=xml
}
