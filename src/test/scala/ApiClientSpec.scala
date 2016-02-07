package com.cats

import org.scalatest._

class ApiClientSpec extends FlatSpec with Matchers {
  "ApiClient" should "have tests" in {
    true should === (true)
  }

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
  "Fact" should "parse JSON content" in {
    val factsJson = """{"facts": ["Tylenol and chocolate are both poisionous to cats."], "success": "true"}"""
    Fact.
  }
}
