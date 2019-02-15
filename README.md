# Requirements

- Java 8

# Stack

- Gradle
- Spring Boot
- Spring WebFlux
- Reactor Adapter
- RxJava2
- Lombok

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
./gradlew light_test
``

Parse and process the companies_light.json file (4 entries) following the coding test specs.


``
./gradlew medium_test
``

Parse and process the companies_medium.json file (100-110 entries) following the coding test specs.


``
./gradlew heavy_test_wo_ipstack
`` 

Parse and process the companies.json file (18-19k entries) following the coding test specs, without the IpStack and Aggregation part.
Generated CSVs are in src/test/generated/ by default.


``
Remove @Ignore on CompaniesServiceTests#it_should_complete_the_coding_test_heavy and launch "./gradlew heavy_test" 
`` 

__/!\ It will blow your IpStack API free rate limit and you may be banned for too many simultaneous requests.__

Parse and process the companies.json file (18-19k entries) following the coding test specs.

# Results

_Using Reactive Non Blocking IO and given the 10k hits free limit of IpStack._

It took 45s to :
* Fully process 10k entries (parsing, csv, ipStack call and aggregation)
* Half process the remaining ~8k entries (parsing, csv)





