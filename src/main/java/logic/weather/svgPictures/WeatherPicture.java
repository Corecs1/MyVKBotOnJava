package logic.weather.svgPictures;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;

import java.io.*;

public class WeatherPicture {
    private final String temp;
    private final String city;
    private final String country;
    private final String description;
    private final String wind;
    private final String pressure;
    private final String humidity;

    public WeatherPicture(String temp, String city, String country, String description, String wind, String pressure, String humidity) {
        this.temp = temp;
        this.city = city;
        this.country = country;
        this.description = description;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    // Тут можно прочитать внутрянку
//    private Document createSVGDocument() throws IOException {
//        String path = "src/main/resources/WeatherCascade.svg";
//        String parser = XMLResourceDescriptor.getXMLParserClassName();
//        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
//        return factory.createDocument(path);
//    }

    // Метод для преобразования готового SVG к PNG/JPEG
//    public File getPicture() {
//        OutputStream pngStream = null;
//
//        String svgUriInput = "src/main/resources/WeatherCascade.svg";
//        TranscoderInput inputSvgImage = new TranscoderInput(svgUriInput);
//        try {
//            pngStream = new FileOutputStream("src/main/resources/WeatherCascade.png");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        TranscoderOutput outputPngImage = new TranscoderOutput(pngStream);
//        PNGTranscoder myConverter = new PNGTranscoder();
//        try {
//            myConverter.transcode(inputSvgImage, outputPngImage);
//        } catch (TranscoderException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                pngStream.flush();
//                pngStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

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

    private String setSVGText() {
        String svgText = String.format("<svg width=\"400\" height=\"460\" viewBox=\"0 0 400 460\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                "<g style=\"mix-blend-mode:darken\">\n" +
                "<rect width=\"400\" height=\"460\" rx=\"15\" fill=\"url(#paint0_linear_7_2)\"/>\n" +
                "</g>\n" +
                "<text fill=\"black\" xml:space=\"preserve\" style=\"white-space: pre\" font-family=\"Inter\" font-size=\"38\" font-weight=\"600\" letter-spacing=\"0em\"><tspan x=\"214.508\" y=\"94.3182\">%s</tspan></text>\n" +
                "<text fill=\"black\" xml:space=\"preserve\" style=\"white-space: pre\" font-family=\"Inter\" font-size=\"30\" letter-spacing=\"0em\"><tspan x=\"9\" y=\"76.4091\">%s</tspan></text>\n" +
                "<text fill=\"black\" xml:space=\"preserve\" style=\"white-space: pre\" font-family=\"Inter\" font-size=\"30\" letter-spacing=\"0em\"><tspan x=\"9\" y=\"167.409\">%s</tspan></text>\n" +
                "<text fill=\"black\" xml:space=\"preserve\" style=\"white-space: pre\" font-family=\"Inter\" font-size=\"30\" letter-spacing=\"0em\"><tspan x=\"10\" y=\"239.409\">%s</tspan></text>\n" +
                "<text fill=\"black\" xml:space=\"preserve\" style=\"white-space: pre\" font-family=\"Inter\" font-size=\"26\" font-weight=\"300\" letter-spacing=\"0em\"><tspan x=\"10\" y=\"395.955\">Ветер: %s</tspan></text>\n" +
                "<text fill=\"black\" xml:space=\"preserve\" style=\"white-space: pre\" font-family=\"Inter\" font-size=\"26\" font-weight=\"300\" letter-spacing=\"0em\"><tspan x=\"10\" y=\"348.955\">Давление: %s</tspan></text>\n" +
                "<text fill=\"black\" xml:space=\"preserve\" style=\"white-space: pre\" font-family=\"Inter\" font-size=\"26\" font-weight=\"300\" letter-spacing=\"0em\"><tspan x=\"10\" y=\"301.955\">Влажность: %s</tspan></text>\n" +
                "<defs>\n" +
                "<linearGradient id=\"paint0_linear_7_2\" x1=\"200\" y1=\"0\" x2=\"200\" y2=\"460\" gradientUnits=\"userSpaceOnUse\">\n" +
                "<stop offset=\"0.119792\" stop-color=\"#F7FF97\" stop-opacity=\"0.96\"/>\n" +
                "<stop offset=\"1\" stop-color=\"#F9FFB4\" stop-opacity=\"0\"/>\n" +
                "</linearGradient>\n" +
                "</defs>\n" +
                "</svg>\n", this.temp, this.city, this.country, this.description, this.wind, this.pressure, this.humidity);
        return svgText;
    }
}