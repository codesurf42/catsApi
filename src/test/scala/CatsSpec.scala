package com.cats

import java.io.File
import java.net.URI
import org.scalatest._

class CatsSpec extends FlatSpec with Matchers

class CategoriesSpec extends CatsSpec {
  "Categories" should "parse xml content" in {
    val catsXml =
      """<?xml version="1.0"?>
        |<response>
        |  <data>
        |    <categories>
        |      <category>
        |        <id>1</id>
        |        <name>hats</name>
        |      </category>
        |      <category>
        |        <id>2</id>
        |        <name>space</name>
        |      </category>
        |    </categories>
        |  </data>
        |</response>
      """.stripMargin
    Categories.parseXml(catsXml).map(_.name).toSet shouldBe Set("hats", "space")
    assert(Categories.parseXml(catsXml).head.id >= 1)
  }
}

class FactSpec extends CatsSpec {
  "Fact" should "parse JSON content" in {
    val factsJson = """{"facts": ["Tylenol and chocolate are both poisionous to cats."], "success": "true"}"""
    Fact.parseJson(factsJson) shouldBe "Tylenol and chocolate are both poisionous to cats."
  }
}

class CatImageSpec extends CatsSpec {

  "CatImage" should "parse XML content" in {
    val xml =
      """<?xml version="1.0"?>
        |<response>
        |  <data>
        |    <images>
        |      <image>
        |        <url>http://25.media.tumblr.com/tumblr_m3zxc1DryY1qgg1kko1_400.gif</url>
        |        <id>amr</id>
        |        <source_url>http://thecatapi.com/?id=amr</source_url>
        |      </image>
        |    </images>
        |  </data>
        |</response>""".stripMargin
    CatImage.parse(xml) shouldBe Some(new URI("http://25.media.tumblr.com/tumblr_m3zxc1DryY1qgg1kko1_400.gif"))
  }

  "CatImage" should "parse broken XML content" in {
    val xml =
      """<?xml version="1.0"?>
        |<response>
        |  <data>
        |    <images>
        |      <image>
        |        <id>amr</id>
        |        <source_url>http://thecatapi.com/?id=amr</source_url>
        |      </image>
        |    </images>
        |  </data>
        |</response>""".stripMargin
    CatImage.parse(
      xml) shouldBe None
  }

  "CatImage" should "write binary files" in {
    val fileContent = "fooJHK".toArray.map(_.toByte)
    val tmpFile = CatImage.imageToFile(fileContent)
    io.Source.fromFile(tmpFile).toArray shouldBe fileContent
    new File(tmpFile).delete()
  }
}

class LinksSpec extends CatsSpec {
  "uriExtension" should "find extension" in {
    val t1 = new Links {}
    t1.getImageExtension(new URI("fooo.bar")) shouldBe None
    t1.getImageExtension(new URI("fooo.png")) shouldBe Some("png")
    t1.getImageExtension(new URI("http://24.media.tumblr.com/tumblr_m43gf6BZzJ1qhwlspo1_500.jpg")) shouldBe Some("jpg")
  }
}
