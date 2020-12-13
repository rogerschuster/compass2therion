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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author roger
 */
public abstract class AbstractSurveyParser implements SurveyParser {

    private static final BigDecimal TO_GRADIANS = BigDecimal.valueOf(400.00).divide(BigDecimal.valueOf(360.00), MathContext.DECIMAL32);
    private static final BigDecimal TO_METRES = BigDecimal.valueOf(0.3048);

    protected BigDecimal degreeToGradians(BigDecimal degree) {
        return degree.multiply(TO_GRADIANS).setScale(2, RoundingMode.HALF_UP);
    }

    protected BigDecimal degreeToPercent(BigDecimal degree) {
        double toRadians = Math.toRadians(degree.doubleValue());
        double percent = Math.tan(toRadians) * 100;
        return BigDecimal.valueOf(percent).setScale(2, RoundingMode.HALF_UP);
    }

    protected BigDecimal decimalFeetToMetres(BigDecimal feet) {
        return feet.multiply(TO_METRES, MathContext.DECIMAL32).setScale(2, RoundingMode.HALF_UP);
    }

}
