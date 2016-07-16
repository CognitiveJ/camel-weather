package weather.model;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;


@CsvRecord(separator = ",", crlf = "UNIX")
public class WeatherModel {

    @DataField(pos = 1)
    private String city;
    @DataField(pos = 2)
    private String country;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @Override
    public String toString() {
        return "WeatherModel{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
