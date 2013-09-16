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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.opendatanode.utils.SolrQueryHelper;

public abstract class FacetFactory {
    
    /**
     * Creates {@code FacetInfo} with its {@code FacetItem} for each facet.
     * @param type type for which we want {@code FacetInfo} component
     * @param facetItems facet information about counts, where key is facet.query and value is count for that query 
     * @return
     */
    public static FacetInfo getFacetInfo(FacetItemType type, Map<String, Integer> facetItems) {
     
        // TODO create component for each FacetInfo
        
        switch (type) {
            case DATA_SET:
                return new FacetInfo("by data set", getDefaultFacetItemList(facetItems));
            case LEGAL_FORM:
                return new FacetInfo("by legal form",
                                     getFacetItemList(new String[] {"Podnikateľ-fyzická osoba ...",
                                                                    "Spoločnosť s ručením obmedzeným",
                                                                    "Združenie (zväz, spolok, spoločnosť, klub ai.)",
                                                                    "Akciová spoločnosť",
                                                                    "iné"},
                                                      new String[] {"legal_form:Podnika*",
                                                                    "legal_form:Spoločno*",
                                                                    "legal_form:Zdru*",
                                                                    "legal_form:Akciov*",
                                                                    "-legal_form:(Podnika* Spoločno* Zdru* Akciov*)"},
                                                      facetItems));
            case SEAT:
                return new FacetInfo("by seat",
                                     getFacetItemList(new String[]{"Banská Bystrica",
                                                                   "Bratislava",
                                                                   "Košice",
                                                                   "Nitra",
                                                                   "Prešov",
                                                                   "Trenčín",
                                                                   "Trnava",
                                                                   "Žilina",
                                                                   "iné"},
                                                      new String[] {"seat:\"Banska Bystrica\"",
                                                                    "seat:Bratislava",
                                                                    "seat:Kosice",
                                                                    "seat:Nitra",
                                                                    "seat:Presov",
                                                                    "seat:Trencin",
                                                                    "seat:Trnava",
                                                                    "seat:Zilina",
                                                                    "-seat:(\"Banska Bystrica\"" +
                                                                             " Bratislava" +
                                                                             " Kosice" +
                                                                             " Nitra" +
                                                                             " Presov" +
                                                                             " Trencin" +
                                                                             " Trnava" +
                                                                             " Zilina)"},
                                                      facetItems));
    
            case DATE_FROM:
                return new FacetInfo("by date from",
                                     getFacetItemList(new String[] {"x < 1990",
                                                                    "1990 <= x < 1995",
                                                                    "1995 <= x < 2000",
                                                                    "2000 <= x < 2005",
                                                                    "2005 <= x < 2010",
                                                                    "2010 <= x"},
                                                      new String[] {SolrQueryHelper.getFacetDateRangeQuery("date_from", 0, 1990),
                                                                    SolrQueryHelper.getFacetDateRangeQuery("date_from", 1990, 1995),
                                                                    SolrQueryHelper.getFacetDateRangeQuery("date_from", 1995, 2000),
                                                                    SolrQueryHelper.getFacetDateRangeQuery("date_from", 2000, 2005),
                                                                    SolrQueryHelper.getFacetDateRangeQuery("date_from", 2005, 2010),
                                                                    SolrQueryHelper.getFacetDateRangeQuery("date_from", 2010, 0)},
                                                      facetItems));
            case DATE_TO:
                return new FacetInfo("by date to",
                                     getFacetItemList(new String[] {"x < 1990",
                                                                    "1990 <= x < 1995",
                                                                    "1995 <= x < 2000",
                                                                    "2000 <= x < 2005",
                                                                    "2005 <= x < 2010",
                                                                    "2010 <= x"},
                                                      new String[] {SolrQueryHelper.getFacetDateRangeQuery("date_to", 0, 1990),
                                                                    SolrQueryHelper.getFacetDateRangeQuery("date_to", 1990, 1995),
                                                                    SolrQueryHelper.getFacetDateRangeQuery("date_to", 1995, 2000),
                                                                    SolrQueryHelper.getFacetDateRangeQuery("date_to", 2000, 2005),
                                                                    SolrQueryHelper.getFacetDateRangeQuery("date_to", 2005, 2010),
                                                                    SolrQueryHelper.getFacetDateRangeQuery("date_to", 2010, 0)},
                                                      facetItems));
            case YEAR:
                return new FacetInfo("by year", getFacetItemList(new String[] {"x < 1990",
                                                                        "1990 <= x < 1995",
                                                                        "1995 <= x < 2000",
                                                                        "2000 <= x < 2005",
                                                                        "2005 <= x < 2010",
                                                                        "2010 <= x"},
                                                          new String[] {"year:[* TO 1989]",
                                                                        "year:[1990 TO 1994]",
                                                                        "year:[1995 TO 1999]",
                                                                        "year:[2000 TO 2004]",
                                                                        "year:[2005 TO 2009]",
                                                                        "year:[2010 TO *]"},
                                                          facetItems));
            case PRICE:
                return new FacetInfo("by price",
                                     getFacetItemList(new String[]{"0",
                                                                   "0 < x < 25000",
                                                                   "25000 <= x < 50000",
                                                                   "50000 <= x < 100000",
                                                                   "100000 <= x < 1000000",
                                                                   "1000000 <= x"},
                                                      new String[]{"price:0",
                                                                   "price:[0.01 TO 24999.99]",
                                                                   "price:[25000 TO 49999.99]",
                                                                   "price:[50000 TO 99999.99]",
                                                                   "price:[100000 TO 999999.99]",
                                                                   "price:[1000000 TO *]"},
                                                      facetItems));
            case CURRENCY:
                return new FacetInfo("by currency",
                                     getFacetItemList(new String[] {"SKK",
                                                                    "EUR",
                                                                    "iné"},
                                                      new String[] {"currency:SKK",
                                                                    "currency:EUR",
                                                                    "-currency:(EUR SKK)"},
                                                      facetItems));
            case VAT:
                return new FacetInfo("by VAT", getFacetItemList(new String[]{"s DPH", "bez DPH"},
                                                                new String[]{"vat_included:true",
                                                                             "vat_included:false"},
                                                                facetItems));
            case PARTY:
                return new FacetInfo("by recipient party",
                                     getFacetItemList(new String[]{"ANO",
                                                                   "KDH",
                                                                   "SDKÚ",
                                                                   "Smer",
                                                                   "iné"},
                                                      new String[]{"recipient_party:ANO",
                                                                   "recipient_party:KDH",
                                                                   "recipient_party:SDK*",
                                                                   "recipient_party:(SMER* Smer*)",
                                                                   "-recipient_party:(ANO KDH SDK* SMER* Smer*)"},
                                                      facetItems));
            case VALUE:
                return new FacetInfo("by value",
                                     getFacetItemList(new String[]{"0",
                                                                   "0 < x < 1000",
                                                                   "1000 <= x < 5000",
                                                                   "5000 <= x < 10000",
                                                                   "10000 <= x"},
                                                      new String[]{"donation_value:0",
                                                                   "donation_value:[0.01 TO 999.99]",
                                                                   "donation_value:[1000 TO 4999.99]",
                                                                   "donation_value:[5000 TO 9999.99]",
                                                                   "donation_value:[10000 TO *]"},
                                                      facetItems));
            default:
                System.out.println("treba pridat facet pre "+type);
                return null;
        }
    }
    
    /**
     * default "category" facets with counts set to 0
     * @return
     */
    public static List<FacetItem> getDefaultFacetItemList() {
        return getDefaultFacetItemList(null);
    }

    /**
     * default "category" facets
     * @param countMapping  count mapped for each facet query
     * @return
     */
    public static List<FacetItem> getDefaultFacetItemList(Map<String, Integer> countMapping) {
        return getFacetItemList(new String[] {"organizácie",
                                              "obstarávanie",
                                              "sponzori politických strán"},
                                new String[] {"type:ORG*",
                                              "type:PRO*",
                                              "type:POL*"},
                                countMapping);
    }
    
    /**
     * for each name with query creates FacetItem and maps facet count for it
     * @param names         strings visible for users for each facet
     * @param queries       queries used for faceting
     * @param countMapping  key = query, value = count number
     * @return
     */
    public static List<FacetItem> getFacetItemList(String[] names, String[] queries, Map<String, Integer> countMapping) {
        List<FacetItem> list = new ArrayList<FacetItem>();
        
        if(names == null || queries == null || names.length != queries.length)
            return null;
        
        for (int i = 0; i < queries.length; i++) {
            int count = getCount(queries[i], countMapping);
            list.add(new FacetItem(names[i], queries[i], count));
        }
        
        return list;
    }
    
    /**
     * We dont know exact key in countMapping, so we have to find key (without tags) which
     * is equal with query
     * @param query facet query (facet.query and fq is the same excluding tags) for which we want to know count
     * @param countMapping facet info
     * @return count for {@code query}
     */
    private static int getCount(String query, Map<String, Integer> countMapping) {
        if (countMapping == null) {
            return 0;
        }
        Set<String> queries = countMapping.keySet();
        for (String queryWithTags : queries) {
            String queryWithoutTag = removeTags(queryWithTags);
            if (queryWithoutTag.equals(query)) {
                return countMapping.get(queryWithTags);
            }
        }
        return 0;
    }

    /**
     * removes facet tags
     * @param query     "{!ex=tag1}query" or "{!ex=tag1,tag2}query"
     * @return          "query"
     */
    private static String removeTags(String query) {
        return query.substring(query.indexOf("}")+1);
    }
}
