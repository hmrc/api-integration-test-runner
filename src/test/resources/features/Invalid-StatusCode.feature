@suite


Feature: This will check the status code & response for Registration Number

  Scenario: Verify the response when name is Invalid character
    Given User sends the following parameter as a request:
      | Parameter      | Value          |
      | Name           | %              |
      | Email          | test@gmail.com |
      | Status         | 04             |
      | Contact Type   | REJR           |
      | Contact Number | 123456789012   |
      | Variation      | false          |
    Then User should receive the following response:
      | Status Code | 400 Bad Request |
      | Response    | Invalid name    |

