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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author roger
 */
public class Cave implements Serializable {

    private static final long serialVersionUID = -4267034400449771615L;
    
    private final List<Survey> surveys = new ArrayList<>();
    private final String name;
    private final Set<Connection> connections = new HashSet<>();

    public Cave(String name) {
        this.name = name;
    }

    public void addSurvey(Survey survey) {
        if (!surveys.contains(survey)) {
            surveys.add(survey);
        }
    }

    public List<Survey> getSurveys() {
        return surveys;
    }

    public String getName() {
        return name;
    }

    public void addConnection(Connection conn) {
        connections.add(conn);
    }

    public Set<Connection> getConnections() {
        return connections;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.name);
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
        final Cave other = (Cave) obj;
        return Objects.equals(this.name, other.name);
    }

}
