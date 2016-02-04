package com.example

object ApiClient {
  def main(args: Array[String]): Unit = {

    val firstArg = args.toList.headOption

    println("Started with parameter: " + firstArg)

    firstArg match {
      case Some("categories") => Cats.categories
      case Some("fact") => Cats.facts
      case _ => Cats.imageToFile
    }
  }
}

object Cats {
  /**
    * Print to stdout an ordered list with the different categories of cats
    *
    * @return
    */
  def categories = ???

  /**
    * Print to stdout a cat fact
    *
    * @return
    */
  def facts = ???

  /**
    * Save (on the temporary folder) an image of a cat (as an image
    * file) and print to stdout the url
    * @return
    */
  def imageToFile = ???
}
