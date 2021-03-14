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
public enum ShotItems {
    LENGTH('L', "length"),
    AZIMUT('A', "compass"),
    INCLINATION('D', "clino"),
    REVERSE_AZIMUT('a', "backcompass"),
    REVERSE_INCLINATION('d', "backclino");

    private final char cha;
    private final String text;

    private ShotItems(char cha, String text) {
        this.cha = cha;
        this.text = text;
    }

    public static ShotItems getByType(char type) {
        for (ShotItems f : values()) {
            if (f.cha == type) {
                return f;
            }
        }

        throw new SurveyException(type + " is a not supported shot item");
    }

    public String getText() {
        return text;
    }

}
