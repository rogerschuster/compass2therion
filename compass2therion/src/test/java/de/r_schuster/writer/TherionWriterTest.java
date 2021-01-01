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
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
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
        StringWriter out = new StringWriter();
        SurveyWriter wrt = new TherionWriter(out);
        wrt.write(charset, cave, true);

        String toString = out.toString();
        System.err.println(toString);

        List<String> asList = Arrays.asList(toString.split(newline));
        assertEquals("encoding UTF-8", asList.get(0));
        assertTrue(asList.contains("equate 5@2 5@1"));
        assertTrue(asList.contains("survey 1 -title \"Katasternummer 7225/10\""));
        assertTrue(asList.contains("survey 2 -title \"Just a test\""));
    }

    @Test
    public void complex() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/dreieingangshoehle.dat");
        SurveyParser parser = new CompassParser();
        Networking nw = new FlatNetworking();
        Cave cave = parser.parse("Dreieingangshöhle", is, Charset.forName("Cp1252"), nw);

        Charset charset = Charset.forName("UTF-8");
        StringWriter out = new StringWriter();
        SurveyWriter wrt = new TherionWriter(out);
        wrt.write(charset, cave);

        String toString = out.toString();
        System.err.println(toString);

        String[] split = toString.split(newline);
        assertEquals("encoding UTF-8", split[0]);
    }

    @Test
    public void dates() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/dates.dat");
        SurveyParser parser = new CompassParser();
        Networking nw = new FlatNetworking();
        Cave cave = parser.parse("Dreieingangshöhle", is, Charset.forName("Cp1252"), nw);

        Charset charset = Charset.forName("UTF-8");
        StringWriter out = new StringWriter();
        SurveyWriter wrt = new TherionWriter(out);
        wrt.write(charset, cave);

        String toString = out.toString();
        System.err.println(toString);
        assertTrue(toString.contains("date 2021.2.1"));
        assertTrue(toString.contains("date 2021.1"));
        assertTrue(toString.contains("date 2022"));
        assertTrue(toString.contains("date 2023"));
        assertTrue(toString.contains("date 1968"));
        assertFalse(toString.contains("date 1900.0.0"));
    }
}
