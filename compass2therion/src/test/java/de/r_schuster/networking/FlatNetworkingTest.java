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
import de.r_schuster.data.NetworkConnection;
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
    public void simpleSurvey() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/kleine_scheuer.dat");
        SurveyParser parser = new CompassParser();
        Cave cave = parser.parse("Kleine Scheuer", is, Charset.forName("Cp1252"));
        Networking nw = new FlatNetworking();
        nw.networking(cave);
        for (NetworkConnection conn : cave.getConnections()) {
            System.err.println(conn.toString());
        }
        assertEquals(1, cave.getConnections().size());
    }

    @Test
    public void complex() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/dreieingangshoehle.dat");
        SurveyParser parser = new CompassParser();
        Cave cave = parser.parse("Dreieingangshöhle", is, Charset.forName("Cp1252"));
        Networking nw = new FlatNetworking();
        nw.networking(cave);
        for (NetworkConnection conn : cave.getConnections()) {
            System.err.println(conn.toString());
        }
        assertEquals(9, cave.getConnections().size());
    }
}
