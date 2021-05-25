package client

import org.apache.http.HttpHost
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.elasticsearch.client.{RestClient, RestClientBuilder}

object ConnectionClient {

  def createClient(host: String = "localhost", port: Int = 9200, schema: String = "http") = { //TODO move defaults to some conf file
    val credentialsProvider: CredentialsProvider = new BasicCredentialsProvider
    //val  credentials = new UsernamePasswordCredentials("<username>", "<password>");
    //credentialsProvider.setCredentials(AuthScope.ANY, credentials)

    RestClient.builder(new HttpHost(host, port, schema)).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
      override def customizeHttpClient(httpClientBuilder: HttpAsyncClientBuilder): HttpAsyncClientBuilder = httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
    }).build
  }

}
