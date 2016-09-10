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
package github.yeori.mnist.db;

import java.util.Iterator;

/**
 * used to iterate on mnistdb( like for loop)
 * 
 * @author chmin.seo
 *
 */
public interface MnistLoop extends Iterator<Mnistlet>, Iterable<Mnistlet>{
	/**
	 * true means next() returns a mnistlet, false means no more mnistlet remaining 
	 */
    public boolean hasNext();
    /**
     * get current mnistlet during iteration
     */
    public Mnistlet next();
    /**
     * number of iteration
     * <ul>
     * <li>support for traditional loop
     * </ul>
     */
    public int size ();
    /**
     * returns i-th mnistlet.
     * <ul>
     * <li> support for traditional loop(index-based loop)
     * <li>0 means first elem, size()-1 means last elem.
     * </ul> 
     */
	public Mnistlet get(int index);

}
