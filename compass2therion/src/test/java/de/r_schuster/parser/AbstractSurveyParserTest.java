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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author roger
 */
public class AbstractSurveyParserTest {

    private final AbstractSurveyParser parser;

    public AbstractSurveyParserTest() {
        parser = new AbstractSurveyParser() {
            @Override
            public Cave parse(String caveName, File file, Charset charset) throws IOException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Cave parse(String caveName, InputStream is, Charset charset) throws IOException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    @Test
    public void degreeToGradians() {
        BigDecimal deg = BigDecimal.valueOf(360);
        BigDecimal degreeToGradians = parser.degreeToGradians(deg);
        assertEquals(new BigDecimal("400.00"), degreeToGradians);

        deg = BigDecimal.valueOf(-5);
        degreeToGradians = parser.degreeToGradians(deg);
        assertEquals(new BigDecimal("-5.56"), degreeToGradians);
    }

    @Test
    public void degreeToPercent() {
        BigDecimal deg = BigDecimal.valueOf(45.00);
        BigDecimal degreeToPercent = parser.degreeToPercent(deg);
        assertEquals(new BigDecimal("100.00"), degreeToPercent);

        deg = BigDecimal.valueOf(20);
        degreeToPercent = parser.degreeToPercent(deg);
        assertEquals(new BigDecimal("36.40"), degreeToPercent);
    }

    @Test
    public void decimalFeetToMetres() {
        BigDecimal feet = BigDecimal.valueOf(100.00);
        BigDecimal metres = parser.decimalFeetToMetres(feet);
        assertEquals(new BigDecimal("30.48"), metres);

        feet = BigDecimal.valueOf(22.38);
        metres = parser.decimalFeetToMetres(feet);
        assertEquals(new BigDecimal("6.82"), metres);
    }
}
