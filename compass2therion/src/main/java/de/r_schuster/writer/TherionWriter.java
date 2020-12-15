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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 *
 * @author roger
 */
public class TherionWriter implements SurveyWriter {
    private static final String COMMENT = "# ";
    
    @Override
    public void write(File file, Charset charset, Cave cave) throws IOException {
        try (OutputStream out = new FileOutputStream(file)) {
            write(out, charset, cave);
        }
    }
    
    @Override
    public void write(OutputStream out, Charset charset, Cave cave) throws IOException {
        BufferedWriter wrt = new BufferedWriter(new OutputStreamWriter(out, charset));
        
        // mode line for editor
        wrt.write("encoding ");
        wrt.write(charset.name());
        wrt.newLine();
        
        // cave name
        wrt.write(COMMENT);
        wrt.write(cave.getName());
        wrt.newLine();
        
        // equate section
        for(Connection conn : cave.getConnections()) {
            wrt.write("equate ");
            wrt.write(conn.getThisStation());
            wrt.write("@");
            wrt.write(conn.getThisSurvey());
            wrt.write(' ');
            wrt.write(conn.getOtherStation());
            wrt.write("@");
            wrt.write(conn.getOtherSurvey());
            wrt.newLine();
        }
        
        wrt.flush();
    }
}
