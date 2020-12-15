package de.r_schuster;

import de.r_schuster.data.Cave;
import de.r_schuster.networking.FlatNetworking;
import de.r_schuster.networking.Networking;
import de.r_schuster.parser.CompassParser;
import de.r_schuster.parser.SurveyParser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class App {

    public static void main(String[] args) throws IOException {
        SurveyParser parser = new CompassParser();

        Networking networking = new FlatNetworking();
        Cave cave = parser.parse(args[0], new File(args[1]), Charset.forName("Cp1252"), networking);

    }
}
