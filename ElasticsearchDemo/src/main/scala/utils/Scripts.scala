package utils

import client.ElasticsearchClient

object Scripts {

  def initElasticsearchAndDefaultData(client: ElasticsearchClient) = {
    client.createIndex(InxName, Path)

    client.insert("click", "demo",
      """{
        |    "item_id":"399",
        |    "name":"Single",
        |    "locale":"tr_TR",
        |    "click":122,
        |    "purchase":904
        |  }""".stripMargin)

    client.insert("click", "demo",
      """{
        |    "item_id":"1086",
        |    "name":"Woo Album #4",
        |    "locale":"tr_TR",
        |    "click":203,
        |    "purchase":606
        |  }""".stripMargin)

    client.insert("click", "demo",
      """{
        |    "item_id":"1116",
        |    "name":"Patient Ninja",
        |    "locale":"tr_TR",
        |    "click":470,
        |    "purchase":298
        |  }""".stripMargin)

    client.refresh(InxName)

  }


  //TODO move this to conf
  private val InxName = "click"
  private val Path = "demo"
}
