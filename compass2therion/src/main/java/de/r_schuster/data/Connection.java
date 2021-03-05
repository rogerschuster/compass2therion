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

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author roger
 */
public class Connection implements Serializable {

    private static final long serialVersionUID = 5076875597096647280L;
    
    private String thisStation;
    private String thisSurvey;
    private String otherStation;
    private String otherSurvey;

    public Connection(String thisStation, String thisSurvey, String otherStation, String otherSurvey) {
        this.thisStation = thisStation;
        this.thisSurvey = thisSurvey;
        this.otherStation = otherStation;
        this.otherSurvey = otherSurvey;
    }

    public void setThisStation(String thisStation) {
        this.thisStation = thisStation;
    }

    public void setThisSurvey(String thisSurvey) {
        this.thisSurvey = thisSurvey;
    }

    public void setOtherStation(String otherStation) {
        this.otherStation = otherStation;
    }

    public void setOtherSurvey(String otherSurvey) {
        this.otherSurvey = otherSurvey;
    }

    public String getThisStation() {
        return thisStation;
    }

    public String getThisSurvey() {
        return thisSurvey;
    }

    public String getOtherStation() {
        return otherStation;
    }

    public String getOtherSurvey() {
        return otherSurvey;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.thisStation);
        hash = 11 * hash + Objects.hashCode(this.thisSurvey);
        hash = 11 * hash + Objects.hashCode(this.otherStation);
        hash = 11 * hash + Objects.hashCode(this.otherSurvey);
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
        final Connection other = (Connection) obj;
        if (!Objects.equals(this.thisStation, other.thisStation)) {
            return false;
        }
        if (!Objects.equals(this.thisSurvey, other.thisSurvey)) {
            return false;
        }
        if (!Objects.equals(this.otherStation, other.otherStation)) {
            return false;
        }
        if (!Objects.equals(this.otherSurvey, other.otherSurvey)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Connection{" + "thisStation=" + thisStation + ", thisSurvey=" + thisSurvey + ", otherStation=" + otherStation + ", otherSurvey=" + otherSurvey + '}';
    }

}
