package sk.opendatanode.utils;

import org.apache.solr.client.solrj.SolrQuery;

import sk.opendatanode.solr.SolrType;

public abstract class SolrQueryHelper {
    
    public static SolrQuery setQuery(String query, String types) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("defType", "edismax");
        
        // zadane vsetky => nie je co riesit
        if(types.contains("1")) {
            solrQuery.setQuery(query);
            return solrQuery;
        }
        // ak nie su vsetky => su zadane 1 alebo 2 typy

        StringBuffer queryValue = new StringBuffer(100);
        queryValue.append("(").append(query).append(")").append(" AND type:");
        
        if(types.length() != 1)
            queryValue.append("(");
        
        if(types.contains("2")) {
            queryValue.append(SolrType.ORGANIZATION.getTypeString());
        }
        if(types.matches("2[34]"))
            queryValue.append(" OR ");
        if(types.contains("3")) {
            queryValue.append(SolrType.PROCUREMENT.getTypeString());
        }
        if(types.matches("34"))
            queryValue.append(" OR ");
        if(types.contains("4")) {
            queryValue.append(SolrType.POLITICAL_PARTY_DONATION.getTypeString());
        }
        if(types.length() != 1)
            queryValue.append(")");
        
        // nastavime to ako filter
        solrQuery.setQuery(queryValue.toString());
        return solrQuery;
    }
}
