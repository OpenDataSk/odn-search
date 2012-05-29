package sk.opendatanode.ui.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import sk.opendatanode.facet.FacetItem;
import sk.opendatanode.solr.SolrServerRep;
import sk.opendatanode.solr.SolrType;
import sk.opendatanode.utils.SolrQueryHelper;

public class SearchResultPage extends Panel {
    
    private static final long serialVersionUID = 1965617550606209510L;
    private List<SolrDocument> resultList = new ArrayList<SolrDocument>();
    private int resultCount = 0;

    public SearchResultPage(String id) {
        super(id);
        
        add(new Label("resultCount", new PropertyModel<Integer>(this, "resultCount")));
        
        MyPageableListView listView = new MyPageableListView("resultList", resultList, SolrServerRep.RESULTS_PER_PAGE);
        add(listView);
        
        add(new PagingNavigator("navigator", listView));
    }
    
    private class MyPageableListView extends PageableListView<SolrDocument> {

        private static final long serialVersionUID = 578459835138276328L;

        public MyPageableListView(String id, List<SolrDocument> resultList, int rowsPerPage) {
            super(id, resultList, rowsPerPage);
        }

        @Override
        protected void populateItem(ListItem<SolrDocument> item) {
            final SolrDocument solrResultItem = item.getModelObject();
            int index = item.getIndex() + 1;
            
            item.add(new Label("itemNumber", index+". "));
            item.add(new ExternalLink("itemUrl",
                    "http://www.opendata.sk/item/"+ solrResultItem.get("id"),
                    getLabel(solrResultItem)));
        }

        private String getLabel(SolrDocument solrResultItem) {
            String type = (String)solrResultItem.get("type");
            // TODO document type as link on the document
            // TODO 1-2 lines from content and highlighting searched query
            if(SolrType.ORGANIZATION.getTypeString().equals(type)) {
                return (String)solrResultItem.get("name");
            } else if(SolrType.PROCUREMENT.getTypeString().equals(type)) {
                return "from: "
                        +solrResultItem.get("customer_ico")
                        +"(ICO), to:"
                        +solrResultItem.get("supplier_ico")
                        +"(ICO)";
            } else if(SolrType.POLITICAL_PARTY_DONATION.getTypeString().equals(type)) {
                return (String) solrResultItem.get("name") != null
                        ? (String) solrResultItem.get("name")
                        : solrResultItem.get("donor_name")
                          +" "
                          +solrResultItem.get("donor_surname");
            }
            return null;
        }
        
    }

    public Map<String, Integer> search(PageParameters parameters) throws IOException, SolrServerException {
        final String q = parameters.get("q").toString("").trim();
        
        SolrQuery solrQuery = new SolrQuery();
        
        if(q.isEmpty()) {
            // chceme mat prazdny result, posielame kvoli facet informaciam
            solrQuery.setQuery("*:*");
            solrQuery.setRows(0);
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
        QueryResponse response = SolrServerRep.getInstance().sendQuery(solrQuery);
        resultCount = (int)response.getResults().getNumFound();
        resultList.addAll(response.getResults());
        
        // vratit z response facet informacie
        return response.getFacetQuery();
    }

    private void setQueryFields(SolrQuery solrQuery) {
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

    private void addFilters(SolrQuery solrQuery, PageParameters parameters) {
        for (StringValue query : parameters.getValues("fq")) {
            solrQuery.addFilterQuery(getTag(query.toString())+query.toString());   
        }
    }

    /**
     * adding tags for correct displaying of facet count
     * @param query
     * @return
     */
    private String getTag(String query) {
        return query.startsWith("type:")
                ? SolrQueryHelper.DEF_TAG
                : SolrQueryHelper.DEF_TAG.replace("type", FacetItem.getQueryFieldName(query));
    }
    
    public int getResultCount() {
        return resultCount;
    }
    
    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }
}
