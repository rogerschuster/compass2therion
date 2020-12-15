/*
 * Copyright (C) 2020 roger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.r_schuster.writer;

import de.r_schuster.data.Cave;
import de.r_schuster.networking.FlatNetworking;
import de.r_schuster.networking.Networking;
import de.r_schuster.parser.CompassParser;
import de.r_schuster.parser.CompassParserTest;
import de.r_schuster.parser.SurveyParser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author roger
 */
public class TherionWriterTest {

    private final String newline = System.getProperty("line.separator");

    public TherionWriterTest() {
    }

    
     @Test
    public void simple() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/kleine_scheuer.dat");
        SurveyParser parser = new CompassParser();
        Networking nw = new FlatNetworking();
        Cave cave = parser.parse("Kleine Scheuer", is, Charset.forName("Cp1252"), nw);

        Charset charset = Charset.forName("UTF-8");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SurveyWriter wrt = new TherionWriter();
        wrt.write(out, charset, cave);

        String res = new String(out.toByteArray(), charset);
        System.err.println(res);

        String[] split = res.split(newline);
        assertEquals("encoding UTF-8", split[0]);
    }
    
    @Test
    public void complex() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/dreieingangshoehle.dat");
        SurveyParser parser = new CompassParser();
        Networking nw = new FlatNetworking();
        Cave cave = parser.parse("Dreieingangshöhle", is, Charset.forName("Cp1252"), nw);

        Charset charset = Charset.forName("UTF-8");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SurveyWriter wrt = new TherionWriter();
        wrt.write(out, charset, cave);

        String res = new String(out.toByteArray(), charset);
        System.err.println(res);

        String[] split = res.split(newline);
        assertEquals("encoding UTF-8", split[0]);
    }
}
