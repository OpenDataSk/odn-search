package sk.opendatanode.ui.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.value.ValueMap;

import sk.opendatanode.solr.SolrServerRep;
import sk.opendatanode.ui.search.paging.PagingNavigator;

public class SearchResultPage extends Panel {
    
    private static final long serialVersionUID = 1965617550606209510L;
    private List<SolrDocument> resultList = new ArrayList<SolrDocument>();
    private PagingNavigator navigator = null;
    private PageableResultListView resultView = null;

    public SearchResultPage(String id, ValueMap parameters) {
        super(id);
        final int page = parameters.getInt("page", 1);
        
        resultView  = new PageableResultListView("resultList",
                resultList,
                page);
        add(resultView);
        
        navigator = new PagingNavigator("navigator", parameters);
        add(navigator);
    }

    public void search(ValueMap parameters) throws IOException, SolrServerException {
//      final String type = parameters.getString("type","1");
        final String query = parameters.getString("q", "").trim();
        final int page = parameters.getInt("page", 1);
        
        if(query.isEmpty())
            return;
                
        SolrDocumentList respList = SolrServerRep.getInstance().sendQuery(query, page-1);
        
        int pagesNumber =(int) (respList.getNumFound() / SolrServerRep.RESULTS_PER_PAGE);
        if((respList.getNumFound() % SolrServerRep.RESULTS_PER_PAGE) != 0)
            pagesNumber++;
        
        resultView.setPage(page);
        resultList.addAll(respList);
        navigator.refreshPaging(pagesNumber, page);
    }

    private class PageableResultListView extends ListView<SolrDocument> {

        private static final long serialVersionUID = -3631484307956485036L;
        private int page = 0;

        public PageableResultListView(String id, List<? extends SolrDocument> list, int page) {
            super(id, list);
        }

        public void setPage(int page) {
            this.page = page;
        }

        @Override
        protected void populateItem(ListItem<SolrDocument> item) {
            final SolrDocument solrResultItem = item.getModelObject();
            int index = (page-1) * SolrServerRep.RESULTS_PER_PAGE + item.getIndex() + 1;
            item.add(new Label("itemNumber", index+". "));
            item.add(new ExternalLink("itemUrl",
                    "http://www.opendata.sk/item/"+ solrResultItem.get("id"),
                    (String)solrResultItem.get("name")));
        }
        
    }
}
