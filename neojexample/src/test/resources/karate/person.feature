Feature: Testing Person CRUD
    Background:
        * def homeURL = 'http://localhost:8080/'
        * def person = {name: 'Hashim', age:2, id:'2017'}



    Scenario: Adding Person
        Given url homeURL.concat("save")
        And request person
        When method post
        Then status 200


    Scenario: Find Person By Id Number
        Given url homeURL.concat("findById")
        And param id = person.id
        When method get
        Then status 200


    # Scenario: Update Person
    # Given url homeURL.concat("update/").concat(person.id)
    # And request {name : "Ahmed", age:220}
    # When method put
    # Then status 200


    Scenario: Delete Person
        Given url homeURL.concat("delete/").concat(person.id)
        When method delete
        Then status 200


    Scenario: Delete Graph
        Given url homeURL.concat("deleteGraph")
        When method get
        Then status 200