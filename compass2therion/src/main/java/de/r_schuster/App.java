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
import java.util.logging.Logger;

public class App {

    private static final Logger LOG = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            LOG.warning("Application expects 3 parameters:");
            LOG.warning("Inputfile Outputfile Cavename");
            LOG.warning("Inputfile: Path to a file in Compass format");
            LOG.warning("Outputfile: Path to a file in Therion format");
            LOG.warning("Cavename: Name of the cave (in quotation marks if it contains blanks or non-alphanumeric characters)");
            return;
        }

        String in = args[0];
        String out = args[1];
        String name = args[2];

        File outputfile = new File(out);
        if (outputfile.exists()) {
            throw new IOException("File " + out + " already exists!");
        }

        SurveyParser parser = new CompassParser();
        Networking networking = new FlatNetworking();
        Cave cave = parser.parse(name, new File(in), Charset.forName("Cp1252"), networking);

        try (Writer wrt = new OutputStreamWriter(new FileOutputStream(outputfile), Charset.forName("UTF-8"))) {
            SurveyWriter writer = new TherionWriter(wrt);
            writer.write(Charset.forName("UTF-8"), cave);
        }
    }
}
