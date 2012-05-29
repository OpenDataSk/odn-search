package sk.opendatanode.ui.search.facet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sk.opendatanode.facet.FacetFactory;
import sk.opendatanode.facet.FacetInfo;
import sk.opendatanode.facet.FacetItemType;
import sk.opendatanode.solr.SolrType;
import sk.opendatanode.utils.SolrQueryHelper;

public class SearchFacetPanel extends Panel{
    
    private static final long serialVersionUID = 4635123246467080919L;
    private List<FacetInfo> facetInfoList = new ArrayList<FacetInfo>();
    private PageParameters parameters = null;
    
    public SearchFacetPanel(String id, PageParameters params, Map<String, Integer> facetItems) {
        super(id);
        
        this.parameters = params;
        add(new FacetListView("facetList", facetInfoList));
        
        // adding default category "dataset" and other categories
        facetInfoList.add(FacetFactory.getFacetInfo(FacetItemType.DATA_SET, facetItems));
        addRestOfInfoList(params, facetItems);
    }

    /**
     * putting together of facets for each type
     * @param params
     * @param facetItems
     */
    private void addRestOfInfoList(PageParameters params, Map<String, Integer> facetItems) {
        if(SolrQueryHelper.hasType(SolrType.ORGANIZATION, params)) {
            add(FacetItemType.LEGAL_FORM, facetItems);
            add(FacetItemType.SEAT, facetItems);
            add(FacetItemType.DATE_FROM, facetItems);
            add(FacetItemType.DATE_TO, facetItems);
        } else if (SolrQueryHelper.hasType(SolrType.PROCUREMENT, params)) {
            add(FacetItemType.YEAR, facetItems);
            add(FacetItemType.PRICE, facetItems);
            add(FacetItemType.CURRENCY, facetItems);
            add(FacetItemType.VAT, facetItems);
        } else if(SolrQueryHelper.hasType(SolrType.POLITICAL_PARTY_DONATION, params)) {
            add(FacetItemType.PARTY, facetItems);
            add(FacetItemType.VALUE, facetItems);
            add(FacetItemType.YEAR, facetItems); 
        }
    }

    private void add(FacetItemType type, Map<String, Integer> facetItems) {
        FacetInfo info = FacetFactory.getFacetInfo(type, facetItems);
        if(info != null)
            facetInfoList.add(info);        
    }

    private class FacetListView extends ListView<FacetInfo> {

        private static final long serialVersionUID = -8448695059163993780L;

        public FacetListView(String id, List<FacetInfo> facetList) {
            super(id, facetList);
        }

        @Override
        protected void populateItem(ListItem<FacetInfo> item) {
            FacetInfo facetInfo = item.getModelObject();
            item.add(new Label("facetName", facetInfo.getName()));
            item.add(new FacetItemPanel("facetItem", facetInfo.getItemList(), parameters));
        }
        
    }
}
