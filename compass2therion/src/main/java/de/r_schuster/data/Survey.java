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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author roger
 */
public class Survey implements Serializable {

    private final List<Shot> shots = new ArrayList<>();
    private final Set<String> cavers = new HashSet<>();

    private String caveName;
    private String name;
    private String comment;
    private LocalDate date;
    private BigDecimal declination;

    public void add(String caver) {
        cavers.add(caver);
    }

    public Set<String> getCavers() {
        return cavers;
    }

    public void add(Shot shot) {
        if (!shots.contains(shot)) {
            shots.add(shot);
        }
    }

    public List<Shot> getShots() {
        return shots;
    }

    public String getCaveName() {
        return caveName;
    }

    public void setCaveName(String caveName) {
        this.caveName = caveName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getDeclination() {
        return declination;
    }

    public void setDeclination(BigDecimal declination) {
        this.declination = declination;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.name);
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
        final Survey other = (Survey) obj;
        return Objects.equals(this.name, other.name);
    }

}
