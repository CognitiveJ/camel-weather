package weather

import org.apache.commons.io.FileUtils
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification
import wssimulator.WSSimulator

import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

import static weather.TestingUtils.copyFromTo
import static weather.TestingUtils.deleteFiles
import static weather.TestingUtils.waitFor


/**
 * Simple test to show a test world integration test - this is an end to end test that
 * will place a csv file onto a directory and wait for sample to do its thing.
 * Note - this is backed by a junit4runner so can be simple executed within an IDE
 * or by using the gradle script
 * ./gradlew.bat -Dtest.single=CallRealWebServiceIntegrationTestSpecification test
 */
@ContextConfiguration(classes = Application.class)
@TestPropertySource(locations = "classpath:test-integration.properties")
//uses real world values
class CallRealWebServiceIntegrationTestSpecification extends Specification {

    public static final String directoryLocation = "/tmp"

    def "CSV Routing with a real service"() {
        setup: "put the CSV into the directory that CAMEL is listening to"
        copyFromTo("/input/weather/cities.csv", new File(directoryLocation + "/cities.csv"))
        and: "start WSSimulator"
        WSSimulator.setPort(8866)
        WSSimulator.addSimulation(getClass().getResource("/ws/weather/sample-get-weather-response.yml").getFile() as File);
        WSSimulator.shutdown()
        when:
        waitFor(5, TimeUnit.SECONDS) //let camel do its thing.
        then: "read the written values back into this test so we can output them to a print stream (or going further by validating the response)"
        String output = FileUtils.readFileToString(new File(directoryLocation + "/output.xml"), Charset.defaultCharset())
        println "output:-------\n$output\n-------"
        true
        cleanup: "delete all files"
        deleteFiles(new File(directoryLocation + "/in.csv"), new File(directoryLocation + "/output.xml"))
        WSSimulator.shutdown()
    }
}
