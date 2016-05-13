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

package stepdefs

import cucumber.api.DataTable
import cucumber.api.scala.{EN, ScalaDsl}
import helper.ConnectionHelper
import models.{NotificationErrorResponse, PushNotificationRequest}
import org.scalatest.Matchers
import spray.json._
import models.PushNotificationRequestProtocol._
import models.NotificationErrorResponseProtocol._
import java.util.List

class StatusCodeIntegrationSteps extends ScalaDsl with EN with Matchers {

  var response:(String, List[String]) = null
  val OK = "200"

  Given( """^User sends the following parameter as a request:$""") { (params: DataTable) =>
    val data = params.asMap(classOf[String], classOf[String])
    val name = data.get("Name")
    val email = data.get("Email")
    val status = data.get("Status")
    val contact_type = data.get("Contact Type")
    val contact_number = data.get("Contact Number")
    val variation = data.get("Variation")

    val pushNotificationRequest = PushNotificationRequest(name, email, status, contact_type, contact_number, variation)
    val json = pushNotificationRequest.toJson

    response = ConnectionHelper.createConnection(json.toString, data.get("Registration Number"))
  }

  Then( """^User should receive the following response:$""") { (params: DataTable) =>
    val data = params.asMap(classOf[String], classOf[String])
    val expectedReason = data.get("Response")
    val expectedStatusCode = data.get("Status Code")

    if (!expectedStatusCode.contains(OK)) {
      val resp = response._1.parseJson.convertTo[NotificationErrorResponse]
      resp.reason should be(expectedReason)
    }

    response._2.get(0) contains expectedStatusCode should be(true)
  }

  Given( """^User send the invalid json parameter as a request:$""") { (params: DataTable) =>
    val data = params.asMap(classOf[String], classOf[String])
    val variation = data.get("Variation")
    val json: String = "{ \"variation\": " + variation + "}"
    response = ConnectionHelper.createConnection(json, data.get("Registration Number"))
  }
}