/*
 * Copyright 2016 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils.config

import java.net.URL
import javax.net.ssl.HttpsURLConnection
import java.net.HttpURLConnection

case class Configuration(API_URL: String, HTTP_CONNECTION: ((String, String)=>HttpURLConnection))

object Configuration {

  lazy val environment: Environment.Name = {
    val environmentProperty = System.getProperty("environment", "env1").toLowerCase
    environmentProperty match {
      case "env1" => Environment.env1
      case "env2" => Environment.env2
      case "env3" => Environment.env3
      case _ => throw new IllegalArgumentException(s"Environment '$environmentProperty' not known")
    }
  }

  lazy val settings: Configuration = create()

  private def create(): Configuration = {

    environment match {

      case Environment.env1 => new Configuration(
        API_URL = "http://www.env1url.com/",
        HTTP_CONNECTION = createHTTPConnection
        )

      case Environment.env2 =>
        new Configuration(
          API_URL = "http://www.env2url.com/",
          HTTP_CONNECTION = createHTTPConnection
        )

      case Environment.env3 =>
        new Configuration(
          API_URL = "https://www.env3url.com/",
          HTTP_CONNECTION = createHTTPsConnection
        )

      case _ => throw new IllegalArgumentException(s"Environment '$environment' not known")
    }
  }

  def createHTTPConnection(url: String, regNumber: String) = (new URL(s"$url$regNumber")).openConnection.asInstanceOf[HttpURLConnection]
  def createHTTPsConnection(url: String, regNumber: String) = (new URL(s"$url$regNumber")).openConnection.asInstanceOf[HttpsURLConnection]

}

object Environment extends Enumeration {
  type Name = Value
  val env1, env2, env3, env4 = Value
}
