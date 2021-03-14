/*
 * Copyright (C) 2021 roger
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

import java.io.Serializable;

/**
 * Transfer object for survey date. It doesn't do any kind of validation.
 * @author roger
 */
public class SurveyDate implements Serializable {

    private static final long serialVersionUID = -207619146665470323L;
    
    private final int year;
    private final int month;
    private final int day;

    public SurveyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.year;
        hash = 37 * hash + this.month;
        hash = 37 * hash + this.day;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SurveyDate other = (SurveyDate) obj;
        if (this.year != other.year) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        return this.day == other.day;
    }

}
