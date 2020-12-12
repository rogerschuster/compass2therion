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

import de.r_schuster.data.AzimutUnits;
import de.r_schuster.data.Cave;
import de.r_schuster.data.Dimensions;
import de.r_schuster.data.DimensionsAssociations;
import de.r_schuster.data.ShotItems;
import de.r_schuster.data.InclinationUnits;
import de.r_schuster.data.LengthUnits;
import de.r_schuster.data.Shot;
import de.r_schuster.data.Survey;
import de.r_schuster.exceptions.SurveyException;
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
public class CompassParserTest {

    public CompassParserTest() {
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
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/kleine_scheuer.dat");
        SurveyParser parser = new CompassParser();
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
        assertEquals(AzimutUnits.DEGREES, survey.getAzimutUnit());
        assertEquals(LengthUnits.METRES, survey.getLengthUnit());
        assertEquals(LengthUnits.METRES, survey.getDimensionUnit());
        assertEquals(InclinationUnits.DEGREES, survey.getInclinationUnit());
        assertEquals(Dimensions.LEFT, survey.getDimensionsOrder().get(1));
        assertEquals(Dimensions.RIGHT, survey.getDimensionsOrder().get(2));
        assertEquals(Dimensions.UP, survey.getDimensionsOrder().get(3));
        assertEquals(Dimensions.DOWN, survey.getDimensionsOrder().get(4));
        assertEquals(ShotItems.LENGTH, survey.getShotItemsOrder().get(1));
        assertEquals(ShotItems.INCLINATION, survey.getShotItemsOrder().get(2));
        assertEquals(ShotItems.AZIMUT, survey.getShotItemsOrder().get(3));
        assertEquals(ShotItems.REVERSE_INCLINATION, survey.getShotItemsOrder().get(4));
        assertEquals(ShotItems.REVERSE_AZIMUT, survey.getShotItemsOrder().get(5));
        assertTrue(survey.isReverse());
        assertEquals(DimensionsAssociations.FROM, survey.getDimensionsAssociation());
        List<Shot> shots = survey.getShots();
        //   assertEquals(6, shots.size());
    }

    @Test
    public void compatibility() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/compatibility.dat");
        SurveyParser parser = new CompassParser();
        Cave cave = parser.parse("Test Hole", is, Charset.forName("Cp1252"));

        Survey survey1 = cave.getSurveys().get(0);
        assertEquals(LocalDate.of(1968, 9, 21), survey1.getDate());
    }

    @Test
    public void invalid() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/invalid.dat");
        SurveyParser parser = new CompassParser();
        try {
            Cave cave = parser.parse("Test Hole", is, Charset.forName("Cp1252"));
            fail("Needs to throw an exception");
        } catch (SurveyException e) {
            assertTrue(e.getMessage().contains("Error while reading line 6"));
        }
    }
}
