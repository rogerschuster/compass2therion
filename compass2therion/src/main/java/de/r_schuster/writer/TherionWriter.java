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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 *
 * @author roger
 */
public class TherionWriter extends BufferedWriter implements SurveyWriter {

    private static final String COMMENT = "# ";

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

        flush();
    }
}
