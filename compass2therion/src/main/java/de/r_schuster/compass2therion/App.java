package de.r_schuster.compass2therion;

import de.r_schuster.compass2therion.data.Cave;
import de.r_schuster.compass2therion.networking.FlatNetworking;
import de.r_schuster.compass2therion.networking.Networking;
import de.r_schuster.compass2therion.parser.CompassParser;
import de.r_schuster.compass2therion.parser.SurveyParser;
import de.r_schuster.compass2therion.writer.SurveyWriter;
import de.r_schuster.compass2therion.writer.TherionWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class App {

    private static final Logger LOG = LogManager.getLogger(App.class);
    private static final String NL = System.getProperty("line.separator");

    public static void main(String[] args) throws IOException {
        if (args.length == 0 || "-?".equals(args[0]) || "-h".equals(args[0])) {
            printMessage();
            return;
        }

        String inputfile = null;
        String outputfile = null;
        String cavename = null;
        boolean renameSurvey = false;

        int len = args.length;
        for (int i = 0; i < len; i++) {
            String arg = args[i];
            if ("--input".equalsIgnoreCase(arg) && len >= i + 2) {
                inputfile = args[i + 1];
            } else if ("--output".equalsIgnoreCase(arg) && len >= i + 2) {
                outputfile = args[i + 1];
            } else if ("--cavename".equalsIgnoreCase(arg) && len >= i + 2) {
                cavename = args[i + 1];
            } else if ("--renamesurvey".equalsIgnoreCase(arg)) {
                renameSurvey = true;
            }
        }

        if (inputfile == null || outputfile == null || cavename == null) {
            printMessage();
            return;
        }

        File outfile = new File(outputfile);
        if (outfile.exists()) {
            throw new IOException("File " + outputfile + " already exists!");
        }

        Cave cave;
        try (InputStream is = new FileInputStream(inputfile)) {
            SurveyParser parser = new CompassParser();
            Networking networking = new FlatNetworking();
            cave = parser.parse(cavename, is, Charset.forName("Cp1252"), networking);
        }

        try (Writer wrt = new OutputStreamWriter(new FileOutputStream(outfile), Charset.forName("UTF-8"))) {
            SurveyWriter writer = new TherionWriter(wrt);
            writer.write(Charset.forName("UTF-8"), cave, renameSurvey);
        }
    }

    private static void printMessage() {
        StringBuilder sb = new StringBuilder("USAGE");
        sb.append(NL);
        sb.append("Application expects the following arguments:").append(NL);
        sb.append("--input [PATH TO INPUT FILE] (required)").append(NL);
        sb.append("--output [PATH TO OUTPUT FILE] (required)").append(NL);
        sb.append("--cavename [NAME OF CAVE] (required)").append(NL);
        sb.append("--renamesurvey (optional)").append(NL);
        sb.append("It is recommended to put the arguments in quotation marks.").append(NL);
        sb.append("Example: ").append(NL);
        sb.append("--input \"c:\\caves\\cave.dat\" --output \"c:\\caves\\cave.th\" --cavename \"Big Cave\" --renamesurvey").append(NL);
        sb.append(NL);
        sb.append("If you get a warning message containing \"Survey name contains non-alphanumeric characters\" try the optional argument --renamesurvey").append(NL);
        LOG.info(sb.toString());
    }
}
