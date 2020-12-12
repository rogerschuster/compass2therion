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

import de.r_schuster.exceptions.NotSupportedException;

/**
 *
 * @author roger
 */
public enum Fields {
    LENGTH('L'),
    AZIMUT('A'),
    INCLINATION('D'),
    REVERSE_AZIMUT('a'),
    REVERSE_INCLINATION('d');
    

    private final char cha;

    private Fields(char cha) {
        this.cha = cha;
    }

    public static Fields getByType(char type) {
        for (Fields f : values()) {
            if (f.cha == type) {
                return f;
            }
        }

        throw new NotSupportedException(type + " is a not supported field");
    }

}
