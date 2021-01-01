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
import de.r_schuster.data.SurveyDate;
import de.r_schuster.exceptions.SurveyException;
import de.r_schuster.networking.Networking;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
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
        Cave cave = parser.parse("Kleine Scheuer", is, Charset.forName("Cp1252"), createNetworking());

        assertEquals("Kleine Scheuer", cave.getName());

        assertEquals(2, cave.getSurveys().size());
        Survey survey1 = cave.getSurveys().get(0);
        assertEquals("Höhle", survey1.getCaveName());
        assertEquals("10.1", survey1.getName());
        assertEquals(new SurveyDate(2020, 6, 14), survey1.getDate());
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
        List<Shot> shots1 = survey1.getShots();

        assertEquals(6, shots1.size());
        assertEquals("0", shots1.get(0).getFrom());
        assertEquals("1", shots1.get(0).getTo());
        assertEquals(new BigDecimal("6.82"), shots1.get(0).getLength());
        assertEquals(new BigDecimal("11.00"), shots1.get(0).getInclination());
        assertEquals(new BigDecimal("98.00"), shots1.get(0).getAzimut());
        assertEquals(new BigDecimal("-10.00"), shots1.get(0).getReverseInclination());
        assertEquals(new BigDecimal("276.00"), shots1.get(0).getReverseAzimut());
        assertEquals(new BigDecimal("0"), shots1.get(0).getLeft());
        assertEquals(new BigDecimal("0"), shots1.get(0).getRight());
        assertEquals(new BigDecimal("2.25"), shots1.get(0).getUp());
        assertEquals(new BigDecimal("1.29"), shots1.get(0).getDown());
        assertEquals("Höhleneingang", shots1.get(0).getComment());

        assertEquals("1", shots1.get(1).getFrom());
        assertEquals("2", shots1.get(1).getTo());
        assertEquals(new BigDecimal("9.36"), shots1.get(1).getLength());
        assertEquals(new BigDecimal("9.00"), shots1.get(1).getInclination());
        assertEquals(new BigDecimal("354.00"), shots1.get(1).getAzimut());
        assertEquals(new BigDecimal("-10.00"), shots1.get(1).getReverseInclination());
        assertEquals(new BigDecimal("174.00"), shots1.get(1).getReverseAzimut());
        assertEquals(new BigDecimal("0"), shots1.get(1).getLeft());
        assertEquals(new BigDecimal("0"), shots1.get(1).getRight());
        assertEquals(new BigDecimal("6.19"), shots1.get(1).getUp());
        assertEquals(new BigDecimal("1.59"), shots1.get(1).getDown());
        assertNull(shots1.get(1).getComment());

        assertEquals("2", shots1.get(2).getFrom());
        assertEquals("3", shots1.get(2).getTo());
        assertEquals(new BigDecimal("6.06"), shots1.get(2).getLength());
        assertEquals(new BigDecimal("33.00"), shots1.get(2).getInclination());
        assertEquals(new BigDecimal("29.00"), shots1.get(2).getAzimut());
        assertEquals(new BigDecimal("-33.00"), shots1.get(2).getReverseInclination());
        assertEquals(new BigDecimal("209.00"), shots1.get(2).getReverseAzimut());
        assertEquals(new BigDecimal("2.96"), shots1.get(2).getLeft());
        assertEquals(new BigDecimal("2.58"), shots1.get(2).getRight());
        assertEquals(new BigDecimal("6.21"), shots1.get(2).getUp());
        assertEquals(new BigDecimal("1.69"), shots1.get(2).getDown());
        assertEquals("Stufe", shots1.get(2).getComment());

        assertEquals("3", shots1.get(3).getFrom());
        assertEquals("4", shots1.get(3).getTo());
        assertEquals(new BigDecimal("3.85"), shots1.get(3).getLength());
        assertEquals(new BigDecimal("-3.00"), shots1.get(3).getInclination());
        assertEquals(new BigDecimal("74.00"), shots1.get(3).getAzimut());
        assertEquals(new BigDecimal("3.00"), shots1.get(3).getReverseInclination());
        assertEquals(new BigDecimal("254.00"), shots1.get(3).getReverseAzimut());
        assertEquals(new BigDecimal("0.70"), shots1.get(3).getLeft());
        assertEquals(new BigDecimal("2.20"), shots1.get(3).getRight());
        assertEquals(new BigDecimal("0.87"), shots1.get(3).getUp());
        assertEquals(new BigDecimal("1.90"), shots1.get(3).getDown());
        assertNull(shots1.get(3).getComment());

        Survey survey2 = cave.getSurveys().get(1);
        assertEquals("Höhle", survey2.getCaveName());
        assertEquals("10.2", survey2.getName());
        assertEquals(new SurveyDate(2020, 12, 13), survey2.getDate());
        assertEquals("Just a test", survey2.getComment());
        assertTrue(survey2.getCavers().contains("Häberle"));
        assertTrue(survey2.getCavers().contains("Eisele"));
        assertEquals(new BigDecimal("-1.00"), survey2.getDeclination());
        assertEquals(AzimutUnits.GRADS, survey2.getAzimutUnit());
        assertEquals(LengthUnits.METRES, survey2.getLengthUnit());
        assertEquals(LengthUnits.FEET_DECIMAL, survey2.getDimensionUnit());
        assertEquals(InclinationUnits.PERCENT, survey2.getInclinationUnit());
        assertEquals(Dimensions.UP, survey2.getDimensionsOrder().get(1));
        assertEquals(Dimensions.DOWN, survey2.getDimensionsOrder().get(2));
        assertEquals(Dimensions.LEFT, survey2.getDimensionsOrder().get(3));
        assertEquals(Dimensions.RIGHT, survey2.getDimensionsOrder().get(4));
        assertEquals(ShotItems.AZIMUT, survey2.getShotItemsOrder().get(1));
        assertEquals(ShotItems.LENGTH, survey2.getShotItemsOrder().get(2));
        assertEquals(ShotItems.INCLINATION, survey2.getShotItemsOrder().get(3));
        assertEquals(ShotItems.REVERSE_INCLINATION, survey2.getShotItemsOrder().get(4));
        assertEquals(ShotItems.REVERSE_AZIMUT, survey2.getShotItemsOrder().get(5));
        assertFalse(survey2.isReverse());
        assertEquals(DimensionsAssociations.TO, survey2.getDimensionsAssociation());
        List<Shot> shots2 = survey2.getShots();
        assertEquals(4, shots2.size());

        assertEquals("5", shots2.get(0).getFrom());
        assertEquals("2.1", shots2.get(0).getTo());
        assertEquals(new BigDecimal("11.11"), shots2.get(0).getAzimut());
        assertEquals(new BigDecimal("12.56"), shots2.get(0).getLength());
        assertEquals(new BigDecimal("8.75"), shots2.get(0).getInclination());
        assertNull(shots2.get(0).getReverseInclination());
        assertNull(shots2.get(0).getReverseAzimut());
        assertEquals(new BigDecimal("29.53"), shots2.get(0).getUp());
        assertEquals(new BigDecimal("26.25"), shots2.get(0).getDown());
        assertEquals(new BigDecimal("22.97"), shots2.get(0).getLeft());
        assertEquals(new BigDecimal("19.69"), shots2.get(0).getRight());
        assertEquals("test test", shots2.get(0).getComment());

        assertEquals("2.1", shots2.get(1).getFrom());
        assertEquals("2.2", shots2.get(1).getTo());
        assertEquals(new BigDecimal("27.78"), shots2.get(1).getAzimut());
        assertEquals(new BigDecimal("3.80"), shots2.get(1).getLength());
        assertEquals(new BigDecimal("-12.28"), shots2.get(1).getInclination());
        assertNull(shots2.get(1).getReverseInclination());
        assertNull(shots2.get(1).getReverseAzimut());
        assertEquals(new BigDecimal("26.25"), shots2.get(1).getUp());
        assertEquals(new BigDecimal("13.12"), shots2.get(1).getDown());
        assertEquals(new BigDecimal("9.84"), shots2.get(1).getLeft());
        assertEquals(new BigDecimal("3.28"), shots2.get(1).getRight());
        assertEquals("blah blah", shots2.get(1).getComment());

        assertEquals("2.2", shots2.get(2).getFrom());
        assertEquals("2.3", shots2.get(2).getTo());
        assertEquals(new BigDecimal("50.00"), shots2.get(2).getAzimut());
        assertEquals(new BigDecimal("7.89"), shots2.get(2).getLength());
        assertEquals(new BigDecimal("21.26"), shots2.get(2).getInclination());
        assertNull(shots2.get(2).getReverseInclination());
        assertNull(shots2.get(2).getReverseAzimut());
        assertEquals(new BigDecimal("3.28"), shots2.get(2).getUp());
        assertEquals(new BigDecimal("0.98"), shots2.get(2).getDown());
        assertEquals(new BigDecimal("1.31"), shots2.get(2).getLeft());
        assertEquals(new BigDecimal("3.28"), shots2.get(2).getRight());
        assertNull(shots2.get(2).getComment());
    }

    @Test
    public void compatibility() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/compatibility.dat");
        SurveyParser parser = new CompassParser();
        Cave cave = parser.parse("Test Hole", is, Charset.forName("Cp1252"), createNetworking());

        assertEquals(3, cave.getSurveys().size());

        Survey survey1 = cave.getSurveys().get(0);
        assertEquals(new SurveyDate(68, 9, 21), survey1.getDate());
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
        assertEquals(new SurveyDate(96, 9, 28), survey2.getDate());
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
        assertEquals(new SurveyDate(1997, 10, 12), survey3.getDate());
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
            parser.parse("Test Hole", is, Charset.forName("Cp1252"), createNetworking());
            fail("Needs to throw an exception");
        } catch (SurveyException e) {
            assertTrue(e.getMessage().contains("Error while reading line 6"));
        }
    }

    @Test
    public void defaultDimensionAssociation() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/schreiberhoehle.dat");
        SurveyParser parser = new CompassParser();
        Cave cave = parser.parse("Test Hole", is, Charset.forName("Cp1252"), createNetworking());
        assertEquals(DimensionsAssociations.FROM, cave.getSurveys().get(0).getDimensionsAssociation());
    }

    @Test
    public void obscureUnits() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/obscure_units.dat");
        SurveyParser parser = new CompassParser();
        Cave cave = parser.parse("Test Hole", is, Charset.forName("Cp1252"), createNetworking());
        Survey survey = cave.getSurveys().get(0);
        assertEquals(AzimutUnits.DEGREES, survey.getAzimutUnit());
        assertEquals(InclinationUnits.DEGREES, survey.getInclinationUnit());
        assertEquals(LengthUnits.FEET_DECIMAL, survey.getLengthUnit());
        assertEquals(LengthUnits.FEET_DECIMAL, survey.getDimensionUnit());
        assertEquals(new BigDecimal("5.83"), survey.getShots().get(0).getLength());
        assertEquals(new BigDecimal("10.50"), survey.getShots().get(0).getInclination());
        assertEquals(new BigDecimal("45.00"), survey.getShots().get(0).getAzimut());
        assertEquals(new BigDecimal("10.83"), survey.getShots().get(0).getLeft());
    }

    @Test
    public void incompleteDate() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/brokendate.dat");
        SurveyParser parser = new CompassParser();
        Cave cave = parser.parse("Test Hole", is, Charset.forName("Cp1252"), createNetworking());
        assertEquals(new SurveyDate(68, 10, 0), cave.getSurveys().get(0).getDate());
        assertEquals(new SurveyDate(0, 0, 0), cave.getSurveys().get(1).getDate());
    }

    @Test
    public void incompleteDate2() throws IOException {
        InputStream is = CompassParserTest.class.getResourceAsStream("/parser/dates.dat");
        SurveyParser parser = new CompassParser();
        Cave cave = parser.parse("Test Hole", is, Charset.forName("Cp1252"), createNetworking());
        assertEquals(new SurveyDate(2021, 2, 1), cave.getSurveys().get(0).getDate());
        assertEquals(new SurveyDate(2021, 1, 0), cave.getSurveys().get(1).getDate());
        assertEquals(new SurveyDate(22, 0, 0), cave.getSurveys().get(2).getDate());
        assertEquals(new SurveyDate(2023, 0, 0), cave.getSurveys().get(3).getDate());
        assertEquals(new SurveyDate(0, 0, 0), cave.getSurveys().get(4).getDate());
    }

    private Networking createNetworking() {
        Networking net = new Networking() {
            @Override
            public void networking(Cave cave) {
            }
        };

        return net;
    }
}
