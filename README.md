# Requirements

- Java 8

# Configuration

Put your IpStack API Key in
``src/test/resources/application.properties``

(Without it integration tests with IpStack will fail)

# Run tests

``./gradlew test --rerun-tasks``

It starts a mix of unit and integration tests.

The CSV directory is created or cleaned before each test using the CSV writer.

# Final integration tests

``
./gradlew test --tests "com.afossey.companiescodingtest.service.CompaniesServiceTests.it_should_complete_the_coding_test_light" --rerun-tasks -i
``

Parse and process the companies_light.json file according to the coding test.

``
./gradlew test --tests "com.afossey.companiescodingtest.service.CompaniesServiceTests.it_should_complete_the_coding_test_heavy_without_ip_stack" --rerun-tasks -i
`` 

Parse and process the companies.json file according to the coding test, without the IpStack and Aggregation part.

CSVs are in src/test/generated/ by default.



