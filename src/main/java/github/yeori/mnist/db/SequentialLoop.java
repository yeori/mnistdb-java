package github.yeori.mnist.db;

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

import java.util.Iterator;

class SequentialLoop implements MnistLoop {

    
    private MnistDb db;
    private int pos;

    private Mnistlet mlet = new Mnistlet();
    private int limitIndex;
    
    public SequentialLoop(MnistDb db) {
        this(db, 0, db.size());
    }
    /**
     * 
     * @param db
     * @param start start index of images ( inclusive )
     * @param end end index of images ( exclusive )
     */
    public SequentialLoop(MnistDb db, int start, int end) {
        this.db = db;
        this.pos = start;
        this.limitIndex = end;
    }

    @Override
    public boolean hasNext() {
        return pos < limitIndex ;
    }

    @Override
    public Mnistlet next() {
        mlet.set (pos, db.getNumberAt(pos), db.getRawBytesAt(pos));
        pos ++;
        return mlet;
    }
    @Override
    public Iterator<Mnistlet> iterator() {
        return this;
    }

}
