package weather

import org.apache.camel.EndpointInject
import org.apache.camel.Produce
import org.apache.camel.ProducerTemplate
import org.apache.camel.component.mock.MockEndpoint
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification
import wssimulator.WSSimulator

/**
 * Simple test to show a service calling a mocked out consuming soap service.
 * Note - this is backed by a junit4runner so can be simple executed within an IDE
 * or by using the gradle script
 * ./gradlew.bat -Dtest.single=CallMockedWebServiceSpecification test
 */
@ContextConfiguration(classes = Application.class)
// look at the base Spring class to load the conrext from
// use mock test properties for the routes.
@TestPropertySource(locations = "classpath:test.properties")
class CallMockedWebServiceSpecification extends Specification {


    @EndpointInject(uri = "mock:out")
    protected MockEndpoint mock; //mock endpoint to see what camel sends

    @Produce(uri = "direct:in")
    protected ProducerTemplate template; //get a handle on the start of the route.

    def "CSV Routing through an simulated soap service (CSV is passed in directly and not from the files system)"() {
        setup: "set up the consuming soap services and listen on 55432 and reads the attached simulation file - this test uses "
        WSSimulator.setPort(55432) //shutdown the embedded ws server
        WSSimulator.addSimulation(new File(getClass().getResource("/ws/weather/sample-get-weather-response.yml").toURI()))
        when: "We publish a CSV stream to the template and expect 2 messages to be produced"
        template.sendBody("Cork,Ireland\rDublin,Ireland")
        String csv1 = mock.getReceivedExchanges().get(0).getIn().getBody()
        String csv2 = mock.getReceivedExchanges().get(1).getIn().getBody()
        then: "no validation in this test, simply send the output to the print stream"
        println "------------\n $csv1 \n $csv2 \n -------------------"
        mock.assertIsSatisfied()
        cleanup: "lets shutdown the server now with have finished using it"
        WSSimulator.shutdown() //shutdown the embedded ws server

    }

}
