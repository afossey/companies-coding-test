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

Parse and process the companies_light.json file (4 entries) following the coding test specs.


``
./gradlew test --tests "com.afossey.companiescodingtest.service.CompaniesServiceTests.it_should_complete_the_coding_test_medium" --rerun-tasks -i
``

Parse and process the companies_medium.json file (100-110 entries) following the coding test specs.


``
./gradlew test --tests "com.afossey.companiescodingtest.service.CompaniesServiceTests.it_should_complete_the_coding_test_heavy_without_ip_stack" --rerun-tasks -i
`` 

Parse and process the companies.json file (18-19k entries) following the coding test specs, without the IpStack and Aggregation part.
Generated CSVs are in src/test/generated/ by default.


``
./gradlew test --tests "com.afossey.companiescodingtest.service.CompaniesServiceTests.it_should_complete_the_coding_test_heavy" --rerun-tasks -i
`` 

__/!\ It will blow your IpStack API free rate limit and you may be banned for too many simultaneous requests.__

Parse and process the companies.json file (18-19k entries) following the coding test specs.

# Results

_Using Reactive Non Blocking IO and given the 10k hits free limit of IpStack._

It took 45s to :
* Fully process 10k entries (parsing, csv, ipStack call and aggregation)
* Half process the remaining ~8k entries (parsing, csv)





