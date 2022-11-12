package logic.weather.svgPictures;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import java.io.*;
import java.util.Objects;

public class WeatherPicture {
    private final String temp;
    private final String city;
    private final String country;
    private final String description;
    private final String wind;
    private final String pressure;
    private final String humidity;
    private final String weatherCondition;

    public WeatherPicture(String temp, String city, String country, String description, String wind, String pressure, String humidity, String weatherCondition) {
        this.temp = temp;
        this.city = city;
        this.country = country;
        this.description = description;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
        this.weatherCondition = weatherCondition;
    }

    public void createPngPicture() throws IOException {
        OutputStream pngStream = null;

        try {
            pngStream = new FileOutputStream("src/main/resources/WeatherCascade.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        TranscoderOutput outputPngImage = new TranscoderOutput(pngStream);
        PNGTranscoder myConverter = new PNGTranscoder();
        try {
            myConverter.transcode(new TranscoderInput(createSVG()), outputPngImage);
        } catch (TranscoderException e) {
            e.printStackTrace();
        } finally {
            try {
                assert pngStream != null;
                pngStream.flush();
                pngStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private SVGDocument createSVG() throws IOException {
        String svgText = setSVGText();
        SAXSVGDocumentFactory factory;
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        factory = new SAXSVGDocumentFactory(parser);
        SVGDocument svgPicture = factory.createSVGDocument(null, new StringReader(svgText));
        return svgPicture;
    }

    private String setSVGText() throws IOException {
        String replacement = "%s";
        String svgText = selectSvgCascade();
        String replacedSvgText = svgText
                .replace("Temp", replacement)
                .replace("City", replacement)
                .replace("Country", replacement)
                .replace("Описание", replacement);
        return String.format(replacedSvgText, this.temp, this.city, this.country, this.description, this.humidity, this.pressure, this.wind);
    }

    private String selectSvgCascade() throws IOException {
        String trimmedWeatherCondition = weatherCondition.substring(0, 2);
        if (trimmedWeatherCondition.equals("01")) {
            return readSvgFileByPath("/weatherCascades/Cascade1.svg");
        }
        if (trimmedWeatherCondition.equals("02") || trimmedWeatherCondition.equals("03") || trimmedWeatherCondition.equals("04")) {
            return readSvgFileByPath("/weatherCascades/Cascade2-4.svg");
        }
        if (trimmedWeatherCondition.equals("09") || trimmedWeatherCondition.equals("10")) {
            return readSvgFileByPath("/weatherCascades/Cascade9-10.svg");
        }
        if (trimmedWeatherCondition.equals("11")) {
            return readSvgFileByPath("/weatherCascades/Cascade11.svg");
        }
        if (trimmedWeatherCondition.equals("13")) {
            return readSvgFileByPath("/weatherCascades/Cascade13.svg");
        }
        if (trimmedWeatherCondition.equals("50")) {
            return readSvgFileByPath("/weatherCascades/Cascade50.svg");
        }
        return "Unidentified weather condition";
    }

    private String readSvgFileByPath(String URL) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(URL);
        return new String(Objects.requireNonNull(inputStream).readAllBytes());
    }
}