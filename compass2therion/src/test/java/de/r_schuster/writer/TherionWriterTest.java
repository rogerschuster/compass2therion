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
import de.r_schuster.data.Survey;
import de.r_schuster.data.SurveyDate;
import de.r_schuster.networking.FlatNetworking;
import de.r_schuster.networking.Networking;
import de.r_schuster.parser.CompassParser;
import de.r_schuster.parser.CompassParserTest;
import de.r_schuster.parser.SurveyParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    @Test
    public void dateNull() throws IOException {
        StringWriter out = new StringWriter();
        TherionWriter thw = new TherionWriter(out);
        thw.writeDate(null);
        thw.flush();
        assertTrue(out.toString().isEmpty());
    }

    @Test
    public void date0() throws IOException {
        StringWriter out = new StringWriter();
        TherionWriter thw = new TherionWriter(out);
        SurveyDate date = new SurveyDate(0, 1, 1);
        thw.writeDate(date);
        thw.flush();
        assertTrue(out.toString().isEmpty());
    }

    @Test
    public void yearUnder() throws IOException {
        StringWriter out = new StringWriter();
        TherionWriter thw = new TherionWriter(out);
        SurveyDate date = new SurveyDate(30, 1, 1);
        thw.writeDate(date);
        thw.flush();
        String toString = out.toString();
        assertEquals("date 2030.1.1" + newline, toString);
    }

    @Test
    public void yearOver() throws IOException {
        StringWriter out = new StringWriter();
        TherionWriter thw = new TherionWriter(out);
        SurveyDate date = new SurveyDate(51, 1, 1);
        thw.writeDate(date);
        thw.flush();
        String toString = out.toString();
        assertEquals("date 1951.1.1" + newline, toString);
    }

    @Test
    public void yearFourdigits() throws IOException {
        StringWriter out = new StringWriter();
        TherionWriter thw = new TherionWriter(out);
        SurveyDate date = new SurveyDate(2001, 1, 1);
        thw.writeDate(date);
        thw.flush();
        String toString = out.toString();
        assertEquals("date 2001.1.1" + newline, toString);
    }

    @Test
    public void yearOnly() throws IOException {
        StringWriter out = new StringWriter();
        TherionWriter thw = new TherionWriter(out);
        SurveyDate date = new SurveyDate(2001, 0, 0);
        thw.writeDate(date);
        thw.flush();
        String toString = out.toString();
        assertEquals("date 2001" + newline, toString);
    }

    @Test
    public void yearMonth() throws IOException {
        StringWriter out = new StringWriter();
        TherionWriter thw = new TherionWriter(out);
        SurveyDate date = new SurveyDate(2001, 1, 0);
        thw.writeDate(date);
        thw.flush();
        String toString = out.toString();
        assertEquals("date 2001.1" + newline, toString);
    }

    @Test
    public void avoidSurveyNameCollision() {
        Cave cave = new Cave("Test");
        Survey s1 = new Survey();
        s1.setName("1");
        cave.addSurvey(s1);

        Survey s2 = new Survey();
        s2.setName("?");
        cave.addSurvey(s2);

        Survey s3 = new Survey();
        s3.setName("a");
        cave.addSurvey(s3);

        Survey s4 = new Survey();
        s4.setName("'a");
        cave.addSurvey(s4);

        TherionWriter wrt = new TherionWriter(new StringWriter());
        wrt.renameSurveys(cave);
        assertEquals("1", cave.getSurveys().get(0).getName());
        assertEquals("2", cave.getSurveys().get(1).getName());
        assertEquals("a", cave.getSurveys().get(2).getName());
        assertEquals("3", cave.getSurveys().get(3).getName());
    }

    @Test
    public void renameSurvey() throws IOException {
        Cave cave = loadCave("/writer/renamesurvey.dat", "Testcave");
        StringWriter out = new StringWriter();
        SurveyWriter wrt = new TherionWriter(out);
        wrt.write(StandardCharsets.UTF_8, cave, true);
        String toString = out.toString();

        assertTrue(toString.contains("survey 3 -title")); // a+ renamed to 3
        assertTrue(toString.contains("survey 1 -title")); // original
        assertTrue(toString.contains("survey 4 -title")); // b' renamed to 4
        assertTrue(toString.contains("survey 2 -title")); // original

        assertTrue(toString.contains("equate 0@1 0@3")); // a+ renamed to 3
        assertTrue(toString.contains("equate 5@1 5@4")); // b' renamed to 4
        assertTrue(toString.contains("equate 10@2 10@4")); // b' renamed to 4

    }

    private Cave loadCave(String path, String name) throws IOException {
        InputStream is = TherionWriterTest.class.getResourceAsStream(path);
        SurveyParser parser = new CompassParser();
        Networking nw = new FlatNetworking();
        return parser.parse(name, is, Charset.forName("Cp1252"), nw);
    }
}
