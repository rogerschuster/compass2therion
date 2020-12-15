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
package de.r_schuster.networking;

import de.r_schuster.data.Cave;
import de.r_schuster.data.Connection;
import de.r_schuster.parser.CompassParser;
import de.r_schuster.parser.CompassParserTest;
import de.r_schuster.parser.SurveyParser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author roger
 */
public class FlatNetworkingTest {

    public FlatNetworkingTest() {
    }

    @Test
    public void noConnection() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/networking/no_connection.dat");
        SurveyParser parser = new CompassParser();
        Networking nw = new FlatNetworking();
        Cave cave = parser.parse("Networking", is, Charset.forName("Cp1252"), nw);
        assertEquals(0, cave.getConnections().size());
    }

    @Test
    public void oneConnection() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/networking/one_connection.dat");
        SurveyParser parser = new CompassParser();
        Networking nw = new FlatNetworking();
        Cave cave = parser.parse("Networking", is, Charset.forName("Cp1252"), nw);
        assertEquals(1, cave.getConnections().size());

        Connection conn1 = new Connection("1", "1", "1", "2");

        assertTrue(cave.getConnections().contains(conn1));
    }

    @Test
    public void threeConnection() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/networking/three_connection.dat");
        SurveyParser parser = new CompassParser();
        Networking nw = new FlatNetworking();
        Cave cave = parser.parse("Networking", is, Charset.forName("Cp1252"), nw);
        assertEquals(3, cave.getConnections().size());

        Connection conn1 = new Connection("1", "1", "1", "2");
        Connection conn2 = new Connection("1", "1", "1", "3");
        Connection conn3 = new Connection("1", "2", "1", "3");

        assertTrue(cave.getConnections().contains(conn1));
        assertTrue(cave.getConnections().contains(conn2));
        assertTrue(cave.getConnections().contains(conn3));
    }

    @Test
    public void sixConnection() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/networking/six_connection.dat");
        SurveyParser parser = new CompassParser();
        Networking nw = new FlatNetworking();
        Cave cave = parser.parse("Networking", is, Charset.forName("Cp1252"), nw);
        assertEquals(6, cave.getConnections().size());

        Connection conn1 = new Connection("1", "1", "1", "2");
        Connection conn2 = new Connection("1", "1", "1", "3");
        Connection conn3 = new Connection("1", "2", "1", "3");
        Connection conn4 = new Connection("1", "1", "1", "4");
        Connection conn5 = new Connection("1", "1", "1", "4");
        Connection conn6 = new Connection("1", "2", "1", "4");

        assertTrue(cave.getConnections().contains(conn1));
        assertTrue(cave.getConnections().contains(conn2));
        assertTrue(cave.getConnections().contains(conn3));
        assertTrue(cave.getConnections().contains(conn4));
        assertTrue(cave.getConnections().contains(conn5));
        assertTrue(cave.getConnections().contains(conn6));
    }

    @Test
    public void complex() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/dreieingangshoehle.dat");
        SurveyParser parser = new CompassParser();
        Networking nw = new FlatNetworking();
        Cave cave = parser.parse("Dreieingangshöhle", is, Charset.forName("Cp1252"), nw);
        assertEquals(9, cave.getConnections().size());
    }
}
