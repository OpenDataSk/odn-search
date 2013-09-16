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

package sk.opendatanode.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.DateUtil;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import sk.opendatanode.facet.FacetFactory;
import sk.opendatanode.facet.FacetInfo;
import sk.opendatanode.facet.FacetItem;
import sk.opendatanode.facet.FacetItemType;
import sk.opendatanode.solr.SolrServerRep;
import sk.opendatanode.solr.SolrType;

public abstract class SolrQueryHelper {
    
    public final static String DEF_TAG = "{!tag=type}";
    public final static String DEF_EX_TAG = "{!ex=type}";

    /**
     * Adds to {@code solrQuery} all of needed facet queries and their tags
     * @param solrQuery
     * @param parameters
     */
    public static void addFacetParameters(SolrQuery solrQuery, PageParameters parameters) {
        solrQuery.setFacet(true); // allowing facets
//        List<String> excludeTagsForDefault = new ArrayList<String>();
        
        boolean all = hasTypeAll(parameters);
        
        // pridanie fitlrov pre konkretny type
        // TODO refactoring
        if(all || hasType(SolrType.ORGANIZATION, parameters)) {
            addFacetItemQuery(FacetItemType.LEGAL_FORM, solrQuery);
            addFacetItemQuery(FacetItemType.SEAT, solrQuery);
            addFacetItemQuery(FacetItemType.DATE_FROM, solrQuery);
            addFacetItemQuery(FacetItemType.DATE_TO, solrQuery);
//            excludeTagsForDefault.addAll(Arrays.asList(new String[] {"legal_form",
//                                                                     "seat",
//                                                                     "date_from",
//                                                                     "date_to"}));
        }
        
        if(all || hasType(SolrType.PROCUREMENT, parameters)) {
            addFacetItemQuery(FacetItemType.YEAR, solrQuery);
            addFacetItemQuery(FacetItemType.PRICE, solrQuery);
            addFacetItemQuery(FacetItemType.CURRENCY, solrQuery);
            addFacetItemQuery(FacetItemType.VAT, solrQuery);
//            excludeTagsForDefault.addAll(Arrays.asList(new String[] {"year",
//                                                                     "price",
//                                                                     "currency",
//                                                                     "vat_included"}));
        } 
        
        if(all || hasType(SolrType.POLITICAL_PARTY_DONATION, parameters)) {
            addFacetItemQuery(FacetItemType.PARTY, solrQuery);
            addFacetItemQuery(FacetItemType.VALUE, solrQuery);
            addFacetItemQuery(FacetItemType.YEAR, solrQuery);
//            excludeTagsForDefault.addAll(Arrays.asList(new String[] {"recipient_party",
//                                                                     "donation_value",
//                                                                     "year"}));
        }
        
        // pridame defaultne facet filtre (type)
//        List<FacetItem> list = FacetFactory.getDefaultFacetItemList();
//        for (FacetItem facetItem : list) {
//            String exTag = getExcludeTagForDefault(excludeTagsForDefault);
//            solrQuery.addFacetQuery(exTag+facetItem.getQuery());
//        }
    }

    /**
     * 
     * @param parameters
     * @return {@code true} if has NOT secific type else {@code false}
     */
    public static boolean hasTypeAll(PageParameters parameters) {
        List<StringValue> values = parameters.getValues("fq");
        if (values == null || values.isEmpty()) {
            return true;
        }
        
        for (StringValue stringValue : values) {
            if(stringValue.toString().startsWith("type:"))
                return false;
        }
        
        return true;
    }

//    private static String getExcludeTagForDefault(List<String> excludeTagsForDefault) {
//        String tag = DEF_EX_TAG;
//        for (String exTagToAdd : excludeTagsForDefault) {
//            tag = tag.replace("}", ","+exTagToAdd+"}");
//        }
//        return tag;
//    }


    /**
     * adds facet query with relevant excluding tag
     * for example: facet.query:{!ex=def}type:ORGANIZATION_RECORD
     * 
     * @param type
     * @param solrQuery
     */
    private static void addFacetItemQuery(FacetItemType type, SolrQuery solrQuery) {
        FacetInfo facetInfo = FacetFactory.getFacetInfo(type, null);
        
        if(facetInfo == null)
            return;
        
        for (FacetItem facetItem : facetInfo.getItemList()) {
            String exTag = DEF_EX_TAG.replace("type", facetItem.getQueryFieldName());
            solrQuery.addFacetQuery(exTag+facetItem.getQuery());
        }
    }
    
    /**
     * returns query "field:[{@code startYear} TO {@code endYear}]"<br>
     * for example for 1990 <= date_from < 1995 <br>
     * returns query "date_from:[1989-12-31T23:00:00.000Z TO 1989-12-31T22:59:59.999Z]"<br>
     * 
     * if {@code startYear} is 0 than query looks like "date_from:[* TO ...]", similarly if
     * {@code endYear} is 0
     * 
     * @param field     solr field
     * @param startYear start year including
     * @param endYear   end year
     * @return
     */
    public static String getFacetDateRangeQuery(String field, int startYear, int endYear) {
        Date start = new GregorianCalendar(startYear, 0, 1).getTime();
        Calendar cal = new GregorianCalendar(endYear,0,1);
        cal.add(Calendar.MILLISECOND, -1);
        Date end = cal.getTime();

        return field+
               ":["
               +(startYear == 0 ? "*" : DateUtil.getThreadLocalDateFormat().format(start))
               +" TO "
               +(endYear == 0 ? "*" : DateUtil.getThreadLocalDateFormat().format(end))
               +"]";
    }
    
    /**
     * Checks if parameters have filter query with field type and selected value
     * @param type
     * @param params
     * @return
     */
    public static boolean hasType(SolrType type, PageParameters params) {
        if(params.getValues("fq").isEmpty())
            return false;
        String typeSkratka = "type:"+type.getTypeString().substring(0, 3)+"*";
        return params.getValues("fq").contains(StringValue.valueOf(typeSkratka));
    }

    public static QueryResponse search(PageParameters parameters) throws SolrServerException, IOException {
        final String q = parameters.get("q").toString("").trim();
        
        SolrQuery solrQuery = new SolrQuery();
        
        if(q.isEmpty()) {
            // chceme mat prazdny result, posielame kvoli facet informaciam
            solrQuery.setQuery("*:*");
            if(parameters.getNamedKeys().size() > 0) {
                solrQuery.setRows(SolrServerRep.MAX_RESULT_ROWS);
            } else {
                solrQuery.setRows(0);
            }
        } else {
            solrQuery.setQuery(q);
            solrQuery.setRows(SolrServerRep.MAX_RESULT_ROWS);            
        }
        solrQuery.set("defType", "edismax");
        setQueryFields(solrQuery);
        
        // pridame filtre z parametrov pre stranku do parametrov pre solr query
        addFilters(solrQuery, parameters);
        
        SolrQueryHelper.addFacetParameters(solrQuery, parameters);
        
        // odoslanie
        return SolrServerRep.getInstance().sendQuery(solrQuery);
    }
    
    private static void setQueryFields(SolrQuery solrQuery) {
        solrQuery.set("qf", "name^3 " +
                      "legal_form^0.5 " +
                      "seat " +
                      "ico^2 " +
                      "date_from " +
                      "date_to " +
                      "donor_name^2 " +
                      "donor_surname^2 " +
                      "donor_company^2 " +
                      "donor_ico " +
                      "currency^0.5 " +
                      "donor_address " +
                      "donor_psc " +
                      "donor_city " +
                      "recipient_party^0.75 " +
                      "year " +
                      "accept_date " +
                      "note^0.5 " +
                      "procurement_subject " +
                      "customer_ico " +
                      "supplier_ico");
    }

    private static void addFilters(SolrQuery solrQuery, PageParameters parameters) {
        for (StringValue query : parameters.getValues("fq")) {
            solrQuery.addFilterQuery(getTag(query.toString())+query.toString());   
        }
    }

    /**
     * adding tags for correct displaying of facet count
     * @param query
     * @return
     */
    private static String getTag(String query) {
        return query.startsWith("type:")
                ? SolrQueryHelper.DEF_TAG
                : SolrQueryHelper.DEF_TAG.replace("type", FacetItem.getQueryFieldName(query));
    }
}
