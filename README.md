# Requirements

- Java 8

# Configuration

Put your IpStack API Key in
``src/test/resources/application.properties``

(Without it integration tests with IpStack will fail)

# Run tests

``./gradlew test``

Before each test the CSV directory is created and/or cleaned.

# Final integration tests

``
./gradlew test --tests "com.afossey.companiescodingtest.CompaniesServiceTests.it_should_complete_the_coding_test_light" --rerun-tasks -i
``


``
./gradlew test --tests "com.afossey.companiescodingtest.CompaniesServiceTests.it_should_complete_the_coding_test_heavy_without_ip_stack" --rerun-tasks -i
`` 

CSVs in src/test/generated/.



