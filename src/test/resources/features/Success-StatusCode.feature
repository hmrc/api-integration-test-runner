@suite

Feature: This will check the Success status code & response for valid parameters

  Scenario: Verify the response when user send all the valid parameters
    Given User sends the following parameter as a request:
      | Parameter      | Value          |
      | Name           | TestName       |
      | Email          | test@gmail.com |
      | Status         | 04             |
      | Contact Type   | REJR           |
      | Contact Number | 123456789012   |
      | Variation      | false          |
    Then User should receive the following response:
      | Status Code | 200 |
