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
import java.util.Objects;

/**
 *
 * @author roger
 */
public class Shot implements Serializable {

    private static final long serialVersionUID = 2205653266897270882L;

    private String from;
    private String to;
    private BigDecimal length;
    private BigDecimal inclination;
    private BigDecimal azimut;
    private BigDecimal reverseInclination;
    private BigDecimal reverseAzimut;
    private String comment;
    private BigDecimal left;
    private BigDecimal right;
    private BigDecimal up;
    private BigDecimal down;
   
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getInclination() {
        return inclination;
    }

    public void setInclination(BigDecimal inclination) {
        this.inclination = inclination;
    }

    public BigDecimal getAzimut() {
        return azimut;
    }

    public void setAzimut(BigDecimal azimut) {
        this.azimut = azimut;
    }

    public BigDecimal getReverseInclination() {
        return reverseInclination;
    }

    public void setReverseInclination(BigDecimal reverseInclination) {
        this.reverseInclination = reverseInclination;
    }

    public BigDecimal getReverseAzimut() {
        return reverseAzimut;
    }

    public void setReverseAzimut(BigDecimal reverseAzimut) {
        this.reverseAzimut = reverseAzimut;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getLeft() {
        return left;
    }

    public void setLeft(BigDecimal left) {
        this.left = left;
    }

    public BigDecimal getRight() {
        return right;
    }

    public void setRight(BigDecimal right) {
        this.right = right;
    }

    public BigDecimal getUp() {
        return up;
    }

    public void setUp(BigDecimal up) {
        this.up = up;
    }

    public BigDecimal getDown() {
        return down;
    }

    public void setDown(BigDecimal down) {
        this.down = down;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.from);
        hash = 29 * hash + Objects.hashCode(this.to);
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
        final Shot other = (Shot) obj;
        if (!Objects.equals(this.from, other.from)) {
            return false;
        }
        return Objects.equals(this.to, other.to);
    }

}
