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
package de.r_schuster.data;

import de.r_schuster.exceptions.SurveyException;

/**
 *
 * @author roger
 */
public enum InclinationUnits {
    DEGREES('D', "degrees"),
    GRADS('R', "grads"),
    PERCENT('G', "percent"),
    DEGREES_MINUTES('M', "minutes");

    private final char unit;
    private final String text;

    private InclinationUnits(char unit, String text) {
        this.unit = unit;
        this.text = text;
    }

    public static InclinationUnits getByUnit(char uni) {
        for (InclinationUnits i : values()) {
            if (i.unit == uni) {
                return i;
            }
        }

        throw new SurveyException(uni + " is a not supported inclination unit");
    }

    public String getText() {
        return text;
    }

}
