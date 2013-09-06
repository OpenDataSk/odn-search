/* Copyright (C) 2012 Martin Virag <martin.virag@eea.sk>
 *
 * This file is part of Open Data Node.
 *
 * Open Data Node is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Open Data Node is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Open Data Node.  If not, see <http://www.gnu.org/licenses/>.
 */

package sk.opendatanode.facet;

import java.io.Serializable;
import java.util.List;

public class FacetInfo implements Serializable {

    private static final long serialVersionUID = -1556257426649356316L;
    private String name;
    private List<FacetItem>  itemList = null;
    
    public FacetInfo(String name, List<FacetItem> itemList) {
        super();
        this.name = name;
        this.itemList = itemList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FacetItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<FacetItem> itemList) {
        this.itemList = itemList;
    }
}
