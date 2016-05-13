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

package helper

import java.io.DataOutputStream
import javax.net.ssl.{SSLSession, HostnameVerifier, HttpsURLConnection}

import utils.config.{Configuration, Environment}

import scala.util.{Random, Try}

object ConnectionHelper {

  private lazy val chars = ('A' to 'Z')
  private lazy val registrationNumberRange = (100000 to 999999)
  private lazy val randomNumber = Random

  private def dummyRegistration = "Z" + chars(randomNumber.nextInt(25)) +
    "YZ12345" + registrationNumberRange(randomNumber.nextInt(registrationNumberRange length))

  // Escape security certificates and pass everything in env3 environment
  HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
    override def verify(h: String, s: SSLSession) = if(Environment.env3 == Configuration.environment) true else false
  })

  /**
    * The method establishes the connection with required microservice.
    *
    * @param json sent to microservice
    * @param inputRegNumber replaces dummy Registration number where ever required
    */
  def createConnection(json: String, inputRegNumber: String) = {

    val registrationNumber = if(null != inputRegNumber) inputRegNumber else dummyRegistration
    val apiUrl = Configuration.settings.API_URL
    val connection = Configuration.settings.HTTP_CONNECTION(apiUrl, registrationNumber)

    // connection properties :- To populate the header properties
    connection.setRequestProperty("Content-Type", "application/json")

    connection.setDoOutput(true)
    val wr = new DataOutputStream(connection.getOutputStream)
    wr.writeBytes(json)
    wr.flush
    wr.close

    val inputStream = Try(connection.getInputStream).orElse(Try(connection.getErrorStream))
    val streamString = scala.io.Source.fromInputStream(inputStream.get).mkString
    val responseStatusCode = connection.getHeaderFields.get(null)
    (streamString, responseStatusCode)
  }
}
