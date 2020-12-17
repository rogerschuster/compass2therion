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
package de.r_schuster.writer;

import de.r_schuster.data.Cave;
import de.r_schuster.data.Connection;
import de.r_schuster.data.Dimensions;
import de.r_schuster.data.DimensionsAssociations;
import de.r_schuster.data.Shot;
import de.r_schuster.data.ShotItems;
import de.r_schuster.data.Survey;
import de.r_schuster.exceptions.SurveyException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author roger
 */
public class TherionWriter extends BufferedWriter implements SurveyWriter {

    private static final Logger LOG = Logger.getLogger(TherionWriter.class.getName());
    private static final String COMMENT = "# ";
    private static final DateTimeFormatter SURVEY_DATE = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public TherionWriter(Writer out) {
        super(out);
    }

    private void commentln(String str) throws IOException {
        super.write(COMMENT);
        if (str != null) {
            super.write(str);
        }
        super.newLine();
    }

    private void writeln(String str) throws IOException {
        if (str != null) {
            super.write(str);
        }
        super.newLine();
    }

    private void write(String... str) throws IOException {
        for (String s : str) {
            if (s != null) {
                super.write(s);
            }
        }
    }

    private void writeln(String... str) throws IOException {
        write(str);
        super.newLine();
    }

    @Override
    public void write(Charset charset, Cave cave) throws IOException {
        write(charset, cave, false);
    }

    @Override
    public void write(Charset charset, Cave cave, boolean renameSurveys) throws IOException {

        if (renameSurveys) {
            renameSurveys(cave);
        }

        // mode line for editor
        write("encoding ");
        write(charset.name());
        newLine();
        newLine();

        // cave name
        commentln(cave.getName());

        // equate section
        for (Connection conn : cave.getConnections()) {
            write("equate ", conn.getThisStation(), "@", conn.getThisSurvey(), " ", conn.getOtherStation(), "@", conn.getOtherSurvey());
            newLine();
        }

        newLine();
        newLine();

        // surveys
        for (Survey survey : cave.getSurveys()) {
            try {
                writeSurvey(survey);
            } catch (Exception e) {
                throw new SurveyException("Error while writing survey " + survey.getName(), e);
            }
        }

        newLine();
        newLine();

        flush();
    }

    private void writeSurvey(Survey survey) throws IOException {
        if (survey.getName().matches("^.*\\W.*$")) {
            LOG.log(Level.WARNING, "Survey name {0} contains non-alphanumeric characters. This may cause problems in Therion.", survey.getName());
        }
        writeln("survey ", survey.getName(), " -title \"", survey.getComment(), "\"");
        commentln(survey.getCaveName());
        commentln(survey.getComment());

        writeln("centreline");

        writeln("date ", SURVEY_DATE.format(survey.getDate()));

        for (String team : survey.getCavers()) {
            if (team != null && !team.isEmpty()) {
                writeln("team \"", team, "\"");
            }
        }

        writeln("units length ", survey.getLengthUnit().getText());
        writeln("units compass ", survey.getAzimutUnit().getText());
        writeln("units clino ", survey.getInclinationUnit().getText());
        // in Compass declination always degrees
        if (survey.getDeclination() != null) {
            writeln("declination ", formatNum(survey.getDeclination()), " degrees");
        }
        newLine();

        // data order for regular data
        write("data normal from to ");
        Map<Integer, ShotItems> shotItemsOrder = survey.getShotItemsOrder();
        List<Integer> l = new ArrayList<>(shotItemsOrder.keySet());
        Collections.sort(l);
        for (Integer i : l) {
            ShotItems item = shotItemsOrder.get(i);
            if (!survey.isReverse() && item.equals(ShotItems.REVERSE_AZIMUT)) {
                //
            } else if (!survey.isReverse() && item.equals(ShotItems.REVERSE_INCLINATION)) {
                //
            } else {
                write(item.getText(), " ");
            }
        }
        newLine();

        // survey shots
        for (Shot shot : survey.getShots()) {
            write(shot.getFrom(), " ", shot.getTo(), " ");

            for (Integer i : l) {
                ShotItems item = shotItemsOrder.get(i);
                write(shot, item);
            }

            if (shot.getComment() != null) {
                write(COMMENT, shot.getComment());
            }

            newLine();
        }
        newLine();

        // data order for passage dimensions
        write("data dimensions station ");
        Map<Integer, Dimensions> dimensionsOrder = survey.getDimensionsOrder();
        List<Integer> d = new ArrayList<>(dimensionsOrder.keySet());
        Collections.sort(d);
        for (Integer i : d) {
            Dimensions dim = dimensionsOrder.get(i);
            write(dim.getText(), " ");
        }
        newLine();

        // passage dimensions
        for (Shot shot : survey.getShots()) {
            if (survey.getDimensionsAssociation().equals(DimensionsAssociations.FROM)) {
                write(shot.getFrom(), " ");
            } else {
                write(shot.getTo(), " ");
            }

            for (Integer i : d) {
                Dimensions dim = dimensionsOrder.get(i);
                write(shot, dim);
            }
            newLine();
        }

        newLine();

        writeln("endcentreline");
        writeln("endsurvey");

        newLine();
        newLine();
    }

    private void write(Shot shot, ShotItems item) throws IOException {
        switch (item) {
            case LENGTH:
                write(formatNum(shot.getLength()), " ");
                break;
            case AZIMUT:
                write(formatNum(shot.getAzimut()), " ");
                break;
            case INCLINATION:
                write(formatNum(shot.getInclination()), " ");
                break;
            case REVERSE_AZIMUT:
                write(formatNum(shot.getReverseAzimut()), " ");
                break;
            case REVERSE_INCLINATION:
                write(formatNum(shot.getReverseInclination()), " ");
                break;
            default:
                break;
        }
    }

    private void write(Shot shot, Dimensions dim) throws IOException {
        switch (dim) {
            case DOWN:
                write(formatNum(shot.getDown()), " ");
                break;
            case LEFT:
                write(formatNum(shot.getLeft()), " ");
                break;
            case RIGHT:
                write(formatNum(shot.getRight()), " ");
                break;
            case UP:
                write(formatNum(shot.getUp()), " ");
                break;
            default:
                break;
        }
    }

    private String formatNum(BigDecimal bd) {
        if (bd == null) {
            return "";
        }
        return bd.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private void renameSurveys(Cave cave) {
        int cnt = 1;

        for (Survey survey : cave.getSurveys()) {
            String oldName = survey.getName();
            if (oldName.matches("^.*\\W.*$")) {
                String newName = String.valueOf(cnt);
                LOG.log(Level.INFO, "Renaming old survey {0} to new survey name {1}", new Object[]{oldName, newName});
                // renaming survey
                survey.setName(newName);

                // renaming connections
                for (Connection conn : cave.getConnections()) {
                    if (conn.getThisSurvey().equals(oldName)) {
                        conn.setThisSurvey(newName);
                    }
                    if (conn.getOtherSurvey().equals(oldName)) {
                        conn.setOtherSurvey(newName);
                    }
                }

                cnt++;

            }

        }
    }
}
