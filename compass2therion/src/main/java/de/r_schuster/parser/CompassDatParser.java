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
package de.r_schuster.parser;

import de.r_schuster.data.AzimutUnits;
import de.r_schuster.data.Cave;
import de.r_schuster.data.InclinationUnits;
import de.r_schuster.data.LengthUnits;
import de.r_schuster.data.Shot;
import de.r_schuster.data.Survey;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author roger
 */
public class CompassDatParser implements SurveyParser {

    private static final String FORM_FEED = "\u000C";
    private static final String SUB = "\u001A";

    @Override
    public Cave parse(String caveName, File file, Charset charset) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            return parse(caveName, is, charset);
        }
    }

    @Override
    public Cave parse(String caveName, InputStream is, Charset charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));

        Cave cave = new Cave(caveName);
        Survey survey = new Survey();

        String line = reader.readLine();
        int lno = 1;
        while (line != null) {
            // the first line of a survey contains the cave name
            if (lno == 1) {
                survey.setCaveName(line.trim());
            } // second line: survey name
            else if (lno == 2) {
                parseSurveyName(survey, line);
            } // third line: Survey date and comment
            else if (lno == 3) {
                parseSurveyDateAndComment(survey, line);
            } // fourth line: nothing
            // fifth line: survey team
            else if (lno == 5) {
                parseCavers(survey, line);
            } // sixth line: Declination, format, corrections
            else if (lno == 6) {
                parseDeclinationAndFormat(survey, line);
            } // 10ff lines: Survey data
            else if (lno >= 10 && !FORM_FEED.equals(line) && !SUB.equals(line)) {
                Shot shot = new Shot();
                // TODO
                survey.addShot(shot);
            }

            // End of current survey
            if (FORM_FEED.equals(line)) {
                cave.addSurvey(survey);
                survey = new Survey();
                lno = 0;
            }

            lno++;
            line = reader.readLine();
        }

        return cave;
    }

    private void parseSurveyName(Survey survey, String line) {
        final String needle = "SURVEY NAME:";
        int indexOf = line.indexOf(needle);
        String name = line.substring(indexOf + needle.length());
        name = name.trim();
        survey.setName(name);
    }

    private void parseSurveyDateAndComment(Survey survey, String line) {
        final String dateMatch = "SURVEY DATE:";
        final String commentMatch = "COMMENT:";
        int idx1 = line.indexOf(dateMatch);
        int idx2 = line.indexOf(commentMatch);

        String dateString = line.substring(idx1 + dateMatch.length(), idx2).trim();
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M d yyyy"));
        survey.setDate(date);

        String comment = line.substring(idx2 + commentMatch.length()).trim();
        survey.setComment(comment);
    }

    private void parseCavers(Survey survey, String line) {
        String[] split = line.split(",");
        for (String caver : split) {
            survey.addCaver(caver.trim());
        }
    }

    private void parseDeclinationAndFormat(Survey survey, String line) {
        final String decliMatch = "DECLINATION:";
        final String formatMatch = "FORMAT:";
        final String correctionMatch1 = "CORRECTIONS:";
        int idx1 = line.indexOf(decliMatch);
        int idx2 = line.indexOf(formatMatch);
        int idx3 = line.indexOf(correctionMatch1);

        String decliString = line.substring(idx1 + decliMatch.length(), idx2).trim();
        if (!"".equals(decliString) && !"0.00".equals(decliString)) {
            BigDecimal declination = new BigDecimal(decliString);
            survey.setDeclination(declination);
        }

        String format = line.substring(idx2 + formatMatch.length(), idx3).trim();
        for (int i = 0; i < format.length(); i++) {
            char charAt = format.charAt(i);
            switch (i) {
                // Azimut Unit
                case 0:
                    survey.setAzimutUnit(AzimutUnits.getByUnit(charAt));
                    break;
                // length unit
                case 1:
                    survey.setLengthUnit(LengthUnits.getByUnit(charAt));
                    break;
                // dimension unit
                case 2:
                    survey.setDimensionUnit(LengthUnits.getByUnit(charAt));
                    break;
                // inclination unit
                case 3:
                    survey.setInclinationUnit(InclinationUnits.getByUnit(charAt));
                    break;
                default:
                    break;
            }
        }

        // TODO Corrections and Corrections2
    }
}
