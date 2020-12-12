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
import de.r_schuster.data.Dimensions;
import de.r_schuster.data.DimensionsAssociations;
import de.r_schuster.data.InclinationUnits;
import de.r_schuster.data.LengthUnits;
import de.r_schuster.data.Shot;
import de.r_schuster.data.ShotItems;
import de.r_schuster.data.Survey;
import de.r_schuster.exceptions.SurveyException;
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
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 *
 * @author roger
 */
public class CompassParser extends AbstractSurveyParser {

    private static final String FORM_FEED = "\u000C";
    private static final String SUB = "\u001A";
    private static final DateTimeFormatter SURVEY_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("M d ")
            .appendValueReduced(ChronoField.YEAR, 2, 4, 1900)
            .toFormatter(Locale.forLanguageTag("en-US"));
    private static final String SURVEY_NAME_MATCH = "SURVEY NAME:";
    private static final String SURVEY_DATE_MATCH = "SURVEY DATE:";
    private static final String SURVEY_COMMENT_MATCH = "COMMENT:"; // comment is optional in COMPASS
    private static final String DECLINATION_MATCH = "DECLINATION:";
    private static final String FORMAT_MATCH = "FORMAT:"; // optional in Compass
    private static final String CORRECTIONS_MATCH = "CORRECTIONS:"; // optional in Compass

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
        int pos = 0;

        try {
            while (line != null) {
                pos++;
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
        } catch (SurveyException e) {
            throw new SurveyException("Error while reading line " + pos + " of survey file!", e);
        }

        return cave;
    }

    private void parseSurveyName(final Survey survey, final String line) {
        final int indexOf = line.indexOf(SURVEY_NAME_MATCH);
        String name = line.substring(indexOf + SURVEY_NAME_MATCH.length());
        name = name.trim();
        survey.setName(name);
    }

    private void parseSurveyDateAndComment(final Survey survey, final String line) {
        final int idx1 = line.indexOf(SURVEY_DATE_MATCH);
        final int idx2 = line.indexOf(SURVEY_COMMENT_MATCH);

        String dateString;

        if (idx2 == -1) {
            dateString = line.substring(idx1 + SURVEY_DATE_MATCH.length()).trim();
        } else {
            dateString = line.substring(idx1 + SURVEY_DATE_MATCH.length(), idx2).trim();

            String comment = line.substring(idx2 + SURVEY_COMMENT_MATCH.length()).trim();
            survey.setComment(comment);
        }

        LocalDate date = LocalDate.parse(dateString, SURVEY_DATE_FORMATTER);
        survey.setDate(date);
    }

    private void parseCavers(final Survey survey, final String line) {
        String[] split = line.split(",");
        for (String caver : split) {
            survey.addCaver(caver.trim());
        }
    }

    private void parseDeclinationAndFormat(final Survey survey, final String line) {
        final int idx1 = line.indexOf(DECLINATION_MATCH);
        final int idx2 = line.indexOf(FORMAT_MATCH);
        final int idx3 = line.indexOf(CORRECTIONS_MATCH);

        String decliString;
        if (idx2 == -1 && idx3 == -1) {
            decliString = line.substring(idx1 + DECLINATION_MATCH.length()).trim();
        } else {
            decliString = line.substring(idx1 + DECLINATION_MATCH.length(), idx2).trim();
        }

        if (!"".equals(decliString) && !"0.00".equals(decliString)) {
            BigDecimal declination = new BigDecimal(decliString);
            survey.setDeclination(declination);
        }

        String str = null;
        if (idx2 >= 0 && idx3 == -1) {
            str = line.substring(idx2 + FORMAT_MATCH.length()).trim();
        } else if (idx2 >= 0 && idx3 >= 0) {
            str = line.substring(idx2 + FORMAT_MATCH.length(), idx3).trim();
        }

        if (str != null) {
            char[] format = str.toCharArray(); // can be 11, 12, 13 or 15 elements

            if (format.length < 11 || format.length > 15 || format.length == 14) {
                throw new SurveyException("Format string invalid: " + str);
            }

            survey.setAzimutUnit(AzimutUnits.getByUnit(format[0])); // Azimut Unit
            survey.setLengthUnit(LengthUnits.getByUnit(format[1])); // length unit
            survey.setDimensionUnit(LengthUnits.getByUnit(format[2])); // dimension unit
            survey.setInclinationUnit(InclinationUnits.getByUnit(format[3])); // inclination unit
            survey.getDimensionsOrder().put(1, Dimensions.getByType(format[4])); // first passage dimension
            survey.getDimensionsOrder().put(2, Dimensions.getByType(format[5])); // second passage dimension
            survey.getDimensionsOrder().put(3, Dimensions.getByType(format[6])); // third passage dimension
            survey.getDimensionsOrder().put(4, Dimensions.getByType(format[7])); // fourth passage dimension
            survey.getShotItemsOrder().put(1, ShotItems.getByType(format[8])); // first shot item (length, azimut...)
            survey.getShotItemsOrder().put(2, ShotItems.getByType(format[9])); // second shot item (length, azimut...)
            survey.getShotItemsOrder().put(3, ShotItems.getByType(format[10])); // third shot item (length, azimut...)

            if (format.length == 12 || format.length == 13) {
                if (format[11] == 'B') {
                    survey.setReverse(true);
                } else {
                    survey.setReverse(false);
                }
            }
            if (format.length == 13) {
                survey.setDimensionsAssociation(DimensionsAssociations.getByStation(format[12]));
            }
            if (format.length == 15) {
                survey.getShotItemsOrder().put(4, ShotItems.getByType(format[11])); // fourth shot item (length, azimut...)
                survey.getShotItemsOrder().put(5, ShotItems.getByType(format[12])); // fifth shot item (length, azimut...)
                if (format[13] == 'B') {
                    survey.setReverse(true);
                } else {
                    survey.setReverse(false);
                }
                survey.setDimensionsAssociation(DimensionsAssociations.getByStation(format[14]));
            }

        } else {
            survey.setAzimutUnit(AzimutUnits.DEGREES); // Azimut Unit
            survey.setLengthUnit(LengthUnits.FEET_DECIMAL); // length unit
            survey.setDimensionUnit(LengthUnits.FEET_DECIMAL); // dimension unit
            survey.setInclinationUnit(InclinationUnits.DEGREES); // inclination unit
            survey.getDimensionsOrder().put(1, Dimensions.LEFT); // first passage dimension
            survey.getDimensionsOrder().put(2, Dimensions.UP); // second passage dimension
            survey.getDimensionsOrder().put(3, Dimensions.DOWN); // third passage dimension
            survey.getDimensionsOrder().put(4, Dimensions.RIGHT); // fourth passage dimension
            survey.getShotItemsOrder().put(1, ShotItems.LENGTH); // first shot item (length, azimut...)
            survey.getShotItemsOrder().put(2, ShotItems.AZIMUT); // second shot item (length, azimut...)
            survey.getShotItemsOrder().put(3, ShotItems.INCLINATION); // third shot item (length, azimut...)
            survey.setDimensionsAssociation(DimensionsAssociations.FROM);
            survey.setReverse(false);
        }

        // TODO Corrections and Corrections2
    }
}
