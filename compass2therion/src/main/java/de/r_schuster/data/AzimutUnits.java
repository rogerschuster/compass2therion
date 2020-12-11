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

import de.r_schuster.exceptions.UnknownException;

/**
 *
 * @author roger
 */
public enum AzimutUnits {
    DEGREES('D'),
    GRADS('R'),
    QUADS('Q');

    private final char unit;

    private AzimutUnits(char unit) {
        this.unit = unit;
    }

    public AzimutUnits getByUnit(char uni) {
        AzimutUnits[] values = AzimutUnits.values();
        for (AzimutUnits a : values) {
            if (a.unit == uni) {
                return a;
            }
        }
        throw new UnknownException(uni + " is an unknown azimut unit");
    }
}
