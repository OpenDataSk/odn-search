package sk.opendatanode.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.util.DateUtil;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import sk.opendatanode.facet.FacetFactory;
import sk.opendatanode.facet.FacetInfo;
import sk.opendatanode.facet.FacetItem;
import sk.opendatanode.facet.FacetItemType;
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
        List<String> excludeTagsForDefault = new ArrayList<String>();
        
        // pridanie fitlrov pre konkretny type
        // TODO refactoring
        if(hasType(SolrType.ORGANIZATION, parameters)) {
            addFacetItemQuery(FacetItemType.LEGAL_FORM, solrQuery);
            addFacetItemQuery(FacetItemType.SEAT, solrQuery);
            addFacetItemQuery(FacetItemType.DATE_FROM, solrQuery);
            addFacetItemQuery(FacetItemType.DATE_TO, solrQuery);
            excludeTagsForDefault.addAll(Arrays.asList(new String[] {"legal_form",
                                                                     "seat",
                                                                     "date_from",
                                                                     "date_to"}));
        } else if(hasType(SolrType.PROCUREMENT, parameters)) {
            addFacetItemQuery(FacetItemType.YEAR, solrQuery);
            addFacetItemQuery(FacetItemType.PRICE, solrQuery);
            addFacetItemQuery(FacetItemType.CURRENCY, solrQuery);
            addFacetItemQuery(FacetItemType.VAT, solrQuery);
            excludeTagsForDefault.addAll(Arrays.asList(new String[] {"year",
                                                                     "price",
                                                                     "currency",
                                                                     "vat_included"}));
        } else if(hasType(SolrType.POLITICAL_PARTY_DONATION, parameters)) {
            addFacetItemQuery(FacetItemType.PARTY, solrQuery);
            addFacetItemQuery(FacetItemType.VALUE, solrQuery);
            addFacetItemQuery(FacetItemType.YEAR, solrQuery);
            excludeTagsForDefault.addAll(Arrays.asList(new String[] {"recipient_party",
                                                                     "donation_value",
                                                                     "year"}));
        }
        
        // pridame defaultne facet filtre (type)
        List<FacetItem> list = FacetFactory.getDefaultFacetItemList();
        for (FacetItem facetItem : list) {
            String exTag = getExcludeTagForDefault(excludeTagsForDefault);
            solrQuery.addFacetQuery(exTag+facetItem.getQuery());
        }
    }

    private static String getExcludeTagForDefault(List<String> excludeTagsForDefault) {
        String tag = DEF_EX_TAG;
        for (String exTagToAdd : excludeTagsForDefault) {
            tag = tag.replace("}", ","+exTagToAdd+"}");
        }
        return tag;
    }


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
}
