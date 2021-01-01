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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author roger
 */
public class Survey implements Serializable {

    private final List<Shot> shots = new ArrayList<>();
    private final List<String> cavers = new ArrayList<>();
    private final Map<Integer, Dimensions> dimensionsOrder = new HashMap<>();
    private final Map<Integer, ShotItems> shotItemsOrder = new HashMap<>();

    private String caveName;
    private String name;
    private String comment;
    private SurveyDate date;
    private BigDecimal declination;
    private InclinationUnits inclinationUnit;
    private AzimutUnits azimutUnit;
    private LengthUnits lengthUnit;
    private LengthUnits dimensionUnit;
    private boolean reverse;
    private DimensionsAssociations dimensionsAssociation;

    public void addCaver(String caver) {
        cavers.add(caver);
    }

    public List<String> getCavers() {
        return cavers;
    }

    public void addShot(Shot shot) {
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

    public SurveyDate getDate() {
        return date;
    }

    public void setDate(SurveyDate date) {
        this.date = date;
    }

    public BigDecimal getDeclination() {
        return declination;
    }

    public void setDeclination(BigDecimal declination) {
        this.declination = declination;
    }

    public InclinationUnits getInclinationUnit() {
        return inclinationUnit;
    }

    public void setInclinationUnit(InclinationUnits inclinationUnits) {
        this.inclinationUnit = inclinationUnits;
    }

    public AzimutUnits getAzimutUnit() {
        return azimutUnit;
    }

    public void setAzimutUnit(AzimutUnits azimutUnits) {
        this.azimutUnit = azimutUnits;
    }

    public LengthUnits getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(LengthUnits lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public LengthUnits getDimensionUnit() {
        return dimensionUnit;
    }

    public void setDimensionUnit(LengthUnits dimensionUnit) {
        this.dimensionUnit = dimensionUnit;
    }

    public Map<Integer, Dimensions> getDimensionsOrder() {
        return dimensionsOrder;
    }

    public Map<Integer, ShotItems> getShotItemsOrder() {
        return shotItemsOrder;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public DimensionsAssociations getDimensionsAssociation() {
        return dimensionsAssociation;
    }

    public void setDimensionsAssociation(DimensionsAssociations dimensionsAssociation) {
        this.dimensionsAssociation = dimensionsAssociation;
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
