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
public enum DimensionsAssociations {
    FROM('F'),
    TO('T');

    private final char station;

    DimensionsAssociations(char station) {
        this.station = station;
    }

    public static DimensionsAssociations getByStation(char stat) {
        for (DimensionsAssociations d : values()) {
            if (d.station == stat) {
                return d;
            }
        }

        throw new SurveyException(stat + " is not supported");
    }
}
