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
package de.r_schuster.networking;

import de.r_schuster.data.Cave;
import de.r_schuster.data.NetworkConnection;
import de.r_schuster.data.Shot;
import de.r_schuster.data.Survey;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * In COMPASS all survey stations belong to the same name space. If two surveys
 * contain a station with the same name this station establishes the connection
 * between the surveys.
 *
 * @author roger
 */
public class FlatNetworking implements Networking {

    @Override
    public void networking(Cave cave) {

        Map<String, Set<String>> mapping = new HashMap<>();

        for (Survey surv : cave.getSurveys()) {
            String name = surv.getName();
            Set<String> set = new HashSet<>();
            mapping.put(name, set);
            for (Shot shot : surv.getShots()) {
                String from = shot.getFrom();
                String to = shot.getTo();
                // data reduction. It doesn't matter if the connection is from or to station
                set.add(from);
                set.add(to);
            }
        }

        // compare each set of stations to all _other_ sets of stations
        Iterator<Map.Entry<String, Set<String>>> iterator = mapping.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Set<String>> next = iterator.next();
            String thisSurveyName = next.getKey();
            Set<String> stationsInThisSurvey = next.getValue();
            Iterator<Map.Entry<String, Set<String>>> otherator = mapping.entrySet().iterator();
            while (otherator.hasNext()) {
                Map.Entry<String, Set<String>> other = otherator.next();
                String otherSurveyName = other.getKey();
                if (!otherSurveyName.equals(thisSurveyName)) {
                    Set<String> otherStations = other.getValue();
                    Set<String> intersection = new HashSet<>(stationsInThisSurvey);
                    intersection.retainAll(otherStations);
                    for (String s : intersection) {
                        NetworkConnection conn = new NetworkConnection(s, thisSurveyName, otherSurveyName);
                        cave.addConnection(conn);
                    }
                }
            }
        }

    }

}
