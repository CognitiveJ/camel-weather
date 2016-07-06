package weather.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.stereotype.Component;
import weather.model.WeatherModel;

/**
 * A weather route that will be picked up from a component scan from the {@link weather.Application} class
 */
@Component
public class WeatherRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        //read csv file/stream and split
        from("{{endpoint-start}}")
                .unmarshal().bindy(BindyType.Csv, WeatherModel.class)
                .split(body())
                .to("direct:split");

        //enrich with velocity.
        from("direct:split")
                .to("velocity:template/weather/getWeatherRequest.vm")
                .to("direct:wscall");

        //soap ws call
        from("direct:wscall")
                .setHeader(Exchange.CONTENT_TYPE,
                        constant("text/xml"))
                .to("{{get-weather-endpoint}}")
                .convertBodyTo(String.class)
                .to("{{endpoint-end}}");
    }
}
