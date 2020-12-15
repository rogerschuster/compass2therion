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
import de.r_schuster.data.Shot;
import de.r_schuster.data.ShotItems;
import de.r_schuster.data.Survey;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author roger
 */
public class TherionWriter extends BufferedWriter implements SurveyWriter {

    private static final String COMMENT = "# ";
    private static final DateTimeFormatter SURVEY_DATE = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public TherionWriter(Writer out) {
        super(out);
    }

    private void commentln(String str) throws IOException {
        super.write(COMMENT);
        super.write(str);
        super.newLine();
    }

    private void writeln(String str) throws IOException {
        super.write(str);
        super.newLine();
    }

    private void write(String... str) throws IOException {
        for (String s : str) {
            super.write(s);
        }
    }

    private void writeln(String... str) throws IOException {
        write(str);
        super.newLine();
    }

    @Override
    public void write(Charset charset, Cave cave) throws IOException {

        // mode line for editor
        write("encoding ");
        write(charset.name());
        newLine();

        // cave name
        commentln(cave.getName());

        // equate section
        for (Connection conn : cave.getConnections()) {
            write("equate ", conn.getThisStation(), "@", conn.getThisSurvey(), " ", conn.getOtherStation(), "@", conn.getOtherSurvey());
            newLine();
        }

        // surveys
        for (Survey survey : cave.getSurveys()) {
            writeSurvey(survey);
        }

        flush();
    }

    private void writeSurvey(Survey survey) throws IOException {
        writeln("survey ", survey.getName(), " -title \"", survey.getComment(), "\"");
        commentln(survey.getCaveName());
        commentln(survey.getComment());

        writeln("centreline");

        writeln("date ", SURVEY_DATE.format(survey.getDate()));

        for (String team : survey.getCavers()) {
            writeln("team \"", team, "\"");
        }

        writeln("units length ", survey.getLengthUnit().getText());
        writeln("units compass ", survey.getAzimutUnit().getText());
        writeln("units clino ", survey.getInclinationUnit().getText());
        // in Compass declination always degrees
        writeln("declination ", formatNum(survey.getDeclination()), " degrees");
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
                write("# ", shot.getComment());
            }

            newLine();
        }
        newLine();

        writeln("endcentreline");
        writeln("endsurvey");
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

    private String formatNum(BigDecimal bd) {
        if (bd == null) {
            return "";
        }
        return bd.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
