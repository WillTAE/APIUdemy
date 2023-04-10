Feature: Request example for Udemy

  Scenario: Test Get to the endpoint
    Given I sent a GET request to https://api.github.com URI
    When I get a 200 status code

  Scenario: Validate the amount of entries in the response
    Given I sent a GET request to http://jsonplaceholder.typicode.com URI
    Then I validate there are 10 items on the /users endpoint
  @API
    Scenario: Validate that a specific element is in the response
      Given I sent a GET request to http://jsonplaceholder.typicode.com URI
      Then I validate there is a value: Delphine in the response at /users endpoint

    Scenario: Validate nested value into the response
      Given I sent a GET request to http://jsonplaceholder.typicode.com URI
      Then I should validate the nested value: Kattie Turnpike on the response at /users endpoint