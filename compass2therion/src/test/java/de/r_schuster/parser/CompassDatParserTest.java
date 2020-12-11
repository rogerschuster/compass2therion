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
package de.r_schuster.parser;

import de.r_schuster.data.Cave;
import de.r_schuster.data.Shot;
import de.r_schuster.data.Survey;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author roger
 */
public class CompassDatParserTest {
    
    public CompassDatParserTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void simpleSurvey() throws IOException {
        InputStream is = CompassDatParserTest.class.getResourceAsStream("/parser/kleine_scheuer.dat");
        SurveyParser parser = new CompassDatParser();
        Cave cave = parser.parse("Kleine Scheuer", is, Charset.forName("Cp1252"));
       
        assertEquals("Kleine Scheuer", cave.getName());
        
        assertEquals(1, cave.getSurveys().size());
        Survey survey = cave.getSurveys().get(0);
        assertEquals("Höhle", survey.getCaveName());
        assertEquals("10.1", survey.getName());
        assertEquals(LocalDate.of(2020, 6, 14), survey.getDate());
        assertEquals("Katasternummer 7225/10", survey.getComment());
        assertTrue(survey.getCavers().contains("I. Sachsenmaier"));
        assertTrue(survey.getCavers().contains("R. Schuster"));
        assertEquals(new BigDecimal("3.20"), survey.getDeclination());
        List<Shot> shots = survey.getShots();
     //   assertEquals(6, shots.size());
    }
}
