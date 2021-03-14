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
package de.r_schuster.compass2therion.data;

import de.r_schuster.compass2therion.exceptions.SurveyException;

/**
 *
 * @author roger
 */
public enum LengthUnits {
    METRES('M', "metres"),
    FEET_DECIMAL('D', "feet"),
    FEET_INCHES('I', "inches");

    private final char unit;
    private final String text;

    private LengthUnits(char unit, String text) {
        this.unit = unit;
        this.text = text;
    }

    public static LengthUnits getByUnit(char uni) {
        LengthUnits[] values = LengthUnits.values();
        for (LengthUnits l : values) {
            if (l.unit == uni) {
                return l;
            }
        }

        throw new SurveyException(uni + " is a not supported length unit");
    }

    public String getText() {
        return text;
    }

}
