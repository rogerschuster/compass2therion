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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author roger
 */
public class CompassParserTest {

    public CompassParserTest() {
    }

    @Test
    public void simpleSurvey() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/kleine_scheuer.dat");
        SurveyParser parser = new CompassParser();
        Cave cave = parser.parse("Kleine Scheuer", is, Charset.forName("Cp1252"));

        assertEquals("Kleine Scheuer", cave.getName());

        assertEquals(2, cave.getSurveys().size());
        Survey survey1 = cave.getSurveys().get(0);
        assertEquals("Höhle", survey1.getCaveName());
        assertEquals("10.1", survey1.getName());
        assertEquals(LocalDate.of(2020, 6, 14), survey1.getDate());
        assertEquals("Katasternummer 7225/10", survey1.getComment());
        assertTrue(survey1.getCavers().contains("I. Sachsenmaier"));
        assertTrue(survey1.getCavers().contains("R. Schuster"));
        assertEquals(new BigDecimal("3.20"), survey1.getDeclination());
        assertEquals(AzimutUnits.DEGREES, survey1.getAzimutUnit());
        assertEquals(LengthUnits.METRES, survey1.getLengthUnit());
        assertEquals(LengthUnits.METRES, survey1.getDimensionUnit());
        assertEquals(InclinationUnits.DEGREES, survey1.getInclinationUnit());
        assertEquals(Dimensions.LEFT, survey1.getDimensionsOrder().get(1));
        assertEquals(Dimensions.RIGHT, survey1.getDimensionsOrder().get(2));
        assertEquals(Dimensions.UP, survey1.getDimensionsOrder().get(3));
        assertEquals(Dimensions.DOWN, survey1.getDimensionsOrder().get(4));
        assertEquals(ShotItems.LENGTH, survey1.getShotItemsOrder().get(1));
        assertEquals(ShotItems.INCLINATION, survey1.getShotItemsOrder().get(2));
        assertEquals(ShotItems.AZIMUT, survey1.getShotItemsOrder().get(3));
        assertEquals(ShotItems.REVERSE_INCLINATION, survey1.getShotItemsOrder().get(4));
        assertEquals(ShotItems.REVERSE_AZIMUT, survey1.getShotItemsOrder().get(5));
        assertTrue(survey1.isReverse());
        assertEquals(DimensionsAssociations.FROM, survey1.getDimensionsAssociation());
        List<Shot> shots = survey1.getShots();
        assertEquals(6, shots.size());
    }

    @Test
    public void compatibility() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/compatibility.dat");
        SurveyParser parser = new CompassParser();
        Cave cave = parser.parse("Test Hole", is, Charset.forName("Cp1252"));

        assertEquals(3, cave.getSurveys().size());

        Survey survey1 = cave.getSurveys().get(0);
        assertEquals(LocalDate.of(1968, 9, 21), survey1.getDate());
        assertEquals(AzimutUnits.DEGREES, survey1.getAzimutUnit());
        assertEquals(LengthUnits.FEET_DECIMAL, survey1.getLengthUnit());
        assertEquals(LengthUnits.FEET_DECIMAL, survey1.getDimensionUnit());
        assertEquals(InclinationUnits.DEGREES, survey1.getInclinationUnit());
        assertEquals(Dimensions.LEFT, survey1.getDimensionsOrder().get(1));
        assertEquals(Dimensions.UP, survey1.getDimensionsOrder().get(2));
        assertEquals(Dimensions.DOWN, survey1.getDimensionsOrder().get(3));
        assertEquals(Dimensions.RIGHT, survey1.getDimensionsOrder().get(4));
        assertEquals(ShotItems.LENGTH, survey1.getShotItemsOrder().get(1));
        assertEquals(ShotItems.AZIMUT, survey1.getShotItemsOrder().get(2));
        assertEquals(ShotItems.INCLINATION, survey1.getShotItemsOrder().get(3));
        assertFalse(survey1.isReverse());

        Survey survey2 = cave.getSurveys().get(1);
        assertEquals(LocalDate.of(1996, 9, 28), survey2.getDate());
        assertEquals(AzimutUnits.DEGREES, survey2.getAzimutUnit());
        assertEquals(LengthUnits.FEET_DECIMAL, survey2.getLengthUnit());
        assertEquals(LengthUnits.FEET_DECIMAL, survey2.getDimensionUnit());
        assertEquals(InclinationUnits.DEGREES, survey2.getInclinationUnit());
        assertEquals(Dimensions.LEFT, survey2.getDimensionsOrder().get(1));
        assertEquals(Dimensions.RIGHT, survey2.getDimensionsOrder().get(2));
        assertEquals(Dimensions.UP, survey2.getDimensionsOrder().get(3));
        assertEquals(Dimensions.DOWN, survey2.getDimensionsOrder().get(4));
        assertEquals(ShotItems.LENGTH, survey2.getShotItemsOrder().get(1));
        assertEquals(ShotItems.AZIMUT, survey2.getShotItemsOrder().get(2));
        assertEquals(ShotItems.INCLINATION, survey2.getShotItemsOrder().get(3));
        assertFalse(survey2.isReverse());

        Survey survey3 = cave.getSurveys().get(2);
        assertEquals(LocalDate.of(1997, 10, 12), survey3.getDate());
        assertEquals(AzimutUnits.DEGREES, survey3.getAzimutUnit());
        assertEquals(LengthUnits.FEET_DECIMAL, survey3.getLengthUnit());
        assertEquals(LengthUnits.FEET_DECIMAL, survey3.getDimensionUnit());
        assertEquals(InclinationUnits.DEGREES, survey3.getInclinationUnit());
        assertEquals(Dimensions.LEFT, survey3.getDimensionsOrder().get(1));
        assertEquals(Dimensions.RIGHT, survey3.getDimensionsOrder().get(2));
        assertEquals(Dimensions.UP, survey3.getDimensionsOrder().get(3));
        assertEquals(Dimensions.DOWN, survey3.getDimensionsOrder().get(4));
        assertEquals(ShotItems.LENGTH, survey3.getShotItemsOrder().get(1));
        assertEquals(ShotItems.AZIMUT, survey3.getShotItemsOrder().get(2));
        assertEquals(ShotItems.INCLINATION, survey3.getShotItemsOrder().get(3));
        assertFalse(survey3.isReverse());
    }

    @Test
    public void invalid() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/invalid.dat");
        SurveyParser parser = new CompassParser();
        try {
            parser.parse("Test Hole", is, Charset.forName("Cp1252"));
            fail("Needs to throw an exception");
        } catch (SurveyException e) {
            assertTrue(e.getMessage().contains("Error while reading line 6"));
        }
    }
}
