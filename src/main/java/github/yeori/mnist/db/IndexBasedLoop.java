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
import java.util.List;

class IndexBasedLoop implements MnistLoop {

    private List<Integer> indexes;
    private MnistDb db ;
    private int pos;
    
    public IndexBasedLoop(MnistDb db, List<Integer> indexList) {
        this.db = db;
        this.indexes = indexList;
    }

    @Override
    public boolean hasNext() {
        return pos < indexes.size();
    }

    @Override
    public Mnistlet next() {
        int index = indexes.get(pos);
        pos ++ ;
        return db.get(index);
    }

    @Override
    public Iterator<Mnistlet> iterator() {
        return this;
    }

}
