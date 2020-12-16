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
import de.r_schuster.networking.Networking;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author roger
 */
public class CompassParser extends AbstractSurveyParser {

    private static final Logger LOG = Logger.getLogger(CompassParser.class.getName());

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
    private static final BigDecimal HUNDERED_EIGHTY = new BigDecimal("180.00");
    private static final BigDecimal NINE_NINE_NINE = new BigDecimal("-999.00");

    @Override
    public Cave parse(String caveName, File file, Charset charset, Networking networking) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            return parse(caveName, is, charset, networking);
        }
    }

    @Override
    public Cave parse(String caveName, InputStream is, Charset charset, Networking networking) throws IOException {
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
                    parseShot(survey, line);
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
        } catch (Exception e) {
            throw new SurveyException("Error while reading line " + pos + " of survey file!", e);
        }

        networking.networking(cave); // establishing connections between surveys

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

        String[] split = dateString.trim().split("\\s+");
        dateString = String.join(" ", split);
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

            survey.setDimensionsAssociation(DimensionsAssociations.FROM); // default in older Compass versions

            AzimutUnits azUn;
            if (AzimutUnits.getByUnit(format[0]).equals(AzimutUnits.QUADS)) {
                azUn = AzimutUnits.DEGREES; // workaround for quads (Brunton compass and similar devices)
                LOG.log(Level.INFO, "Converting azimut in quads to degrees. Survey {0}", survey.getName());
            } else {
                azUn = AzimutUnits.getByUnit(format[0]);
            }
            survey.setAzimutUnit(azUn); // Azimut Unit

            LengthUnits lenUn;
            if (LengthUnits.getByUnit(format[1]).equals(LengthUnits.FEET_INCHES)) {
                lenUn = LengthUnits.FEET_DECIMAL; // workaround for feet and inches
                LOG.log(Level.INFO, "Converting length in feet and inches to decimal feet. Survey {0}", survey.getName());
            } else {
                lenUn = LengthUnits.getByUnit(format[1]);
            }
            survey.setLengthUnit(lenUn); // length unit

            LengthUnits dimUn;
            if (LengthUnits.getByUnit(format[2]).equals(LengthUnits.FEET_INCHES)) {
                dimUn = LengthUnits.FEET_DECIMAL; // workaround for feet and inches
                LOG.log(Level.INFO, "Converting passage dimensions in feet and inches to decimal feet. Survey {0}", survey.getName());
            } else {
                dimUn = LengthUnits.getByUnit(format[2]);
            }
            survey.setDimensionUnit(dimUn); // dimension unit

            InclinationUnits incUn;
            if (InclinationUnits.getByUnit(format[3]).equals(InclinationUnits.DEGREES_MINUTES)) {
                incUn = InclinationUnits.DEGREES; // workaround for degrees and minutes
                LOG.log(Level.INFO, "Converting inclination in degrees and minutes to degrees. Survey {0}", survey.getName());
            } else {
                incUn = InclinationUnits.getByUnit(format[3]);
            }
            survey.setInclinationUnit(incUn); // inclination unit

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

    private void parseShot(final Survey survey, final String line) {
        Shot shot = new Shot();

        BigDecimal azimut = null;
        BigDecimal azimutReverse = null;
        BigDecimal inclination = null;
        BigDecimal inclinationReverse = null;
        StringBuilder comments = new StringBuilder();

        String[] split = line.trim().split("\\s+");
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (i == 0) {
                shot.setFrom(s);
            } else if (i == 1) {
                shot.setTo(s);
            } else if (i == 2) {
                BigDecimal len = convertLen(survey, s);
                shot.setLength(len);
            } else if (i == 3) {
                azimut = new BigDecimal(s);
            } else if (i == 4) {
                inclination = new BigDecimal(s);
            } else if (i == 5) {
                BigDecimal left = convertDim(survey, shot, s);
                shot.setLeft(left);
            } else if (i == 6) {
                BigDecimal up = convertDim(survey, shot, s);
                shot.setUp(up);
            } else if (i == 7) {
                BigDecimal down = convertDim(survey, shot, s);
                shot.setDown(down);
            } else if (i == 8) {
                BigDecimal right = convertDim(survey, shot, s);
                shot.setRight(right);
            } // the following items are optional
            // if backsights enabled... 
            else if (survey.isReverse()) {
                // column 9 holds reverse azimut
                if (i == 9) {
                    azimutReverse = new BigDecimal(s);
                }
                // column 10 holds reverse inclination
                if (i == 10) {
                    inclinationReverse = new BigDecimal(s);
                }
                // column 11 may hold either survey flags or beginn of comment
                if (i == 11) {
                    if (s.startsWith("#|")) {
                        // TODO flags are ignored so far
                    } else {
                        comments.append(s).append(' ');
                    }
                }
                // column 12ff may hold comment
                if (i >= 12) {
                    comments.append(s).append(' ');
                }
            } // if backsights are not enabled...
            else if (!survey.isReverse()) {
                // column 9 may hold either survey flags or beginn of comment
                if (i == 9) {
                    if (s.startsWith("#|")) {
                        // TODO flags are ignored so far
                    } else {
                        comments.append(s).append(' ');
                    }
                }
                // column 10ff may hold  comment
                if (i >= 10) {
                    comments.append(s).append(' ');
                }
            }

        }

        /*
        If backsights are enabled the user can omit one of the bussole or 
        inclinometer readings and insert "M" for "Missing" in the cave editor.
        Internally those columns are stored as value -999. Other cave surveying
        software may not know this concept. In this case we need to flip 
        the existing value by 180 or 90 degrees and use the result as replacement
        for the missing value.
         */
        if (survey.isReverse()) {
            if (azimut != null && azimutReverse != null) {
                if (azimut.setScale(2, RoundingMode.HALF_UP).compareTo(NINE_NINE_NINE) == 0) {
                    azimut = flipAzimut(azimutReverse);
                    LOG.log(Level.INFO, "Survey {0}, Shot {1}-{2}: Flipping azimut.", new Object[]{survey.getName(), shot.getFrom(), shot.getTo()});
                } else if (azimutReverse.setScale(2, RoundingMode.HALF_UP).compareTo(NINE_NINE_NINE) == 0) {
                    azimutReverse = flipAzimut(azimut);
                    LOG.log(Level.INFO, "Survey {0}, Shot {1}-{2}: Flipping reverse azimut.", new Object[]{survey.getName(), shot.getFrom(), shot.getTo()});
                }
                shot.setReverseAzimut(convertAzimut(azimutReverse, survey));
            }

            if (inclination != null && inclinationReverse != null) {
                if (inclination.setScale(2, RoundingMode.HALF_UP).compareTo(NINE_NINE_NINE) == 0) {
                    inclination = flipInc(inclinationReverse);
                    LOG.log(Level.INFO, "Survey {0}, Shot {1}-{2}: Flipping inclination.", new Object[]{survey.getName(), shot.getFrom(), shot.getTo()});
                } else if (inclinationReverse.setScale(2, RoundingMode.HALF_UP).compareTo(NINE_NINE_NINE) == 0) {
                    inclinationReverse = flipInc(inclination);
                    LOG.log(Level.INFO, "Survey {0}, Shot {1}-{2}: Flipping reverse inclination.", new Object[]{survey.getName(), shot.getFrom(), shot.getTo()});
                }
                shot.setReverseInclination(inclinationReverse);
            }
        }

        shot.setAzimut(convertAzimut(azimut, survey));
        shot.setInclination(convertInclination(inclination, survey));

        if (comments.length() > 0) {
            shot.setComment(comments.toString().trim());
        }

        survey.addShot(shot);
    }

    private BigDecimal flipInc(BigDecimal inc) {
        return inc.negate();
    }

    private BigDecimal flipAzimut(BigDecimal az) {
        BigDecimal res;
        if (az.setScale(2, RoundingMode.HALF_UP).compareTo(HUNDERED_EIGHTY) > 0) {
            res = az.subtract(HUNDERED_EIGHTY);
        } else {
            res = HUNDERED_EIGHTY.add(az);
        }

        return res;
    }

    private BigDecimal convertAzimut(BigDecimal azimut, final Survey survey) {
        // default unit is degrees.
        if (AzimutUnits.GRADS.equals(survey.getAzimutUnit())) {
            azimut = degreeToGradians(azimut);
        }
        return azimut;
    }

    private BigDecimal convertInclination(BigDecimal inc, final Survey survey) {
        // default unit is degrees.
        if (InclinationUnits.GRADS.equals(survey.getInclinationUnit())) {
            inc = degreeToGradians(inc);
        } else if (InclinationUnits.PERCENT.equals(survey.getInclinationUnit())) {
            inc = degreeToPercent(inc);
        }
        return inc;
    }

    private BigDecimal convertLen(Survey survey, String s) {
        BigDecimal len = new BigDecimal(s); // default unit is decimal feet
        if (LengthUnits.METRES.equals(survey.getLengthUnit())) {
            len = decimalFeetToMetres(len);
        }
        return len;
    }

    /*
    In Compass one can enter "P" for "Passage" instead of a LRUD value. 
    For example if you have a survey station at a T-intersection with a passage 
    opening to the left, you would type "P" in the "left" column of the Compass 
    cave editor. Compass viewer needs this info for not drawing a wall where is
    none. Other cave survey software does not know about this concept why the
    negative number is replaced with zero.
     */
    private BigDecimal convertDim(Survey survey, Shot shot, String s) {
        if (s.contains("-")) {
            LOG.log(Level.INFO, "Survey {0}, Shot {1}-{2}: Setting a LRUD value to zero.", new Object[]{survey.getName(), shot.getFrom(), shot.getTo()});
            return BigDecimal.ZERO;
        } else {
            BigDecimal len = new BigDecimal(s); // default unit is decimal feet
            if (LengthUnits.METRES.equals(survey.getDimensionUnit())) {
                len = decimalFeetToMetres(len);
            }
            return len;
        }
    }
}
