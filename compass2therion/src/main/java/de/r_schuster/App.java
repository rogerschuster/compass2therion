package de.r_schuster;

import de.r_schuster.data.Cave;
import de.r_schuster.networking.FlatNetworking;
import de.r_schuster.networking.Networking;
import de.r_schuster.parser.CompassParser;
import de.r_schuster.parser.SurveyParser;
import de.r_schuster.writer.SurveyWriter;
import de.r_schuster.writer.TherionWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public class App {
    
    public static void main(String[] args) throws IOException {
        File outputfile = new File(args[2]);
        if (outputfile.exists()) {
            throw new IOException("File " + args[2] + " exists!");
        }
        
        SurveyParser parser = new CompassParser();
        Networking networking = new FlatNetworking();
        Cave cave = parser.parse(args[0], new File(args[1]), Charset.forName("Cp1252"), networking);
        
        try (Writer out = new OutputStreamWriter(new FileOutputStream(outputfile), Charset.forName("UTF-8"))) {
            SurveyWriter writer = new TherionWriter(out);
            writer.write(Charset.forName("UTF-8"), cave);
        }
    }
}
