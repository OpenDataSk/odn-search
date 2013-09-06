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
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

public class FacetItem implements Serializable {
 
    private static final long serialVersionUID = 4454142213741287528L;
    private String name;
    private String query;
    private int count;
    
    /**
     * 
     * @param name  what users sees = link label
     * @param query query for solr facet
     * @param count facet count result
     */
    public FacetItem(String name, String query, int count) {
        super();
        this.name = name;
        this.query = query;
        this.count = count;
    }
    
    public String getName() {
        return name;
    }
    
    public String getQuery() {
        return query;
    }
    
    public String getQueryFieldName() {
        return getQueryFieldName(query);
    }
    
    public static String getQueryFieldName(String query) {
        if(query != null && !query.isEmpty()) {
            return query.split(":")[0].replace("-", "");
        }
        return null;
    }
    
    public int getCount() {
        return count;
    }

    /**
     * prida do parametrov linku svoj filter, ak uz tam je
     * to znamena pouzivatel chce odstranit filter tak ho
     * odoberieme
     * @param params
     */
    public void organizeParameters(PageParameters params) {
        boolean atLeastOneFilterAdded = params.getValues("fq").size() > 0;
        
        if(atLeastOneFilterAdded) {
            String thisFieldName = getQueryFieldName();
            boolean onlyOneFilter = params.getValues("fq").size() == 1;
            
            if(onlyOneFilter) { // v podstate sa vztahuje iba na field type
                StringValue fq = params.get("fq");
                boolean isFacetClickedTwice = fq.toString().equals(getQuery());
                boolean isFacetWithTheSameField = thisFieldName.equals(getQueryFieldName(fq.toString()));
                
                if(isFacetClickedTwice) { 
                    params.remove("fq");
                } else if (isFacetWithTheSameField) { // iba jeden fq ineho facet-u -> pridame
                    params.set("fq", StringValue.valueOf(getQuery()));
                } else {
                    params.add("fq", StringValue.valueOf(getQuery()));
                }
            } else {
//                params.put("fq", getModifiedFq((String[]) fq, thisFieldName));
                List<StringValue> values = new ArrayList<StringValue>(params.getValues("fq"));
                modifyFq(values, thisFieldName);
                params.remove("fq");
                for (StringValue val : values)
                    params.add("fq", val);
            }
        } else {
            params.add("fq", StringValue.valueOf(getQuery()));            
        }
    }

    /**
     * If facet was clicked twice, returns fq without its filter. If
     * If user clicked facet with the same "category" before, replaces
     * that facet with his query
     * 
     * @param values
     * @param fieldNameToRemove
     * @return
     */
    private void modifyFq(List<StringValue> values, String fieldNameToRemove) {
        boolean isDefaultFacet = fieldNameToRemove.equals("type");
        boolean isFacetWithTheSameField = false;
                
        StringValue query = StringValue.valueOf(getQuery());
        
        if(values.contains(query)) { // facet clicked twice
            if (isDefaultFacet) {
                values.clear();
                return;
            } else {
                values.remove(query);
                return;
            }
        } else { // musim skontrolovat ci je tam facet s rovnakym fieldom
            for (int i = 0; i < values.size(); i++) {
                isFacetWithTheSameField = fieldNameToRemove.equals(
                                            getQueryFieldName(values.get(i).toString()));
                if(isDefaultFacet && isFacetWithTheSameField) {
                    values.clear();
                    values.add(StringValue.valueOf(getQuery()));
                    return;
                }
                
                if(isFacetWithTheSameField) {
                    values.set(i, StringValue.valueOf(getQuery()));
                    return;
                }
            }
        }
        
        // ak sa dostal sem znamena ze mame pridat novy filter,
        // stacil facet prvy krat z inej "kategorie"
        values.add(StringValue.valueOf(getQuery()));
    }
}
