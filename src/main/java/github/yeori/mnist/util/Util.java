package github.yeori.mnist.util;

/*-
 * #%L
 * JMnistDB
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2016 yeori
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.File;

import github.yeori.mnist.MnistException;

public class Util {

    public static void notNull(String error, Object o) {
        if( o == null ) {
            throw new MnistException(error);
        }
        
    }

    public static void should_be_true(String error, boolean b) {
        if ( !b  ) {
            throw new MnistException(error);
        }
    }

    /**
     * converts byte(-128~127) to int(0~255)
     * @param b
     * @return
     */
    public static int b2i(byte b) {
        return b < 0 ? b + 256 : b;
    }

    public static void should_be_file(String error, File f) {
        if ( f.isDirectory() ) {
            throw new MnistException(error);
        }
    }

    public static void should_exist(String error, File f) {
        if ( !f.exists() ) {
            throw new MnistException(error);
        }
        
    }

    public static void should_be_dir(String error, File dir) {
        if ( dir.isFile() ) {
            throw new MnistException(error);
        }
    }

    public static void should_be_oneOf(String error, String v, String  ... params ) {
        for (int i = 0; i < params.length; i++) {
            if ( params[i].equals(v) ) {
                return ;
            }
        }
    }
    /**
     * replace the given value with <b>replacement</b> only if <b>value equals to 'check'</b>
     * @param value
     * @param check
     * @param replacement
     * @return
     */
    public static String replaceIf(String value, String check, String replacement ) {
        return value.equals(check) ? replacement : value;
    }

    public static void should_be_valid(String error, String value) {
        if ( value == null || value.trim().length() == 0 ) {
            throw new MnistException(error);
        }
    }

    public static void should_not_null(String error, String value) {
        if ( value == null) {
            throw new MnistException(error);
        }
        
    }

    public static void length(String error, byte[] buf, int length) {
        if ( buf.length < length ) {
            throw new MnistException(error);
        }
    }

}
