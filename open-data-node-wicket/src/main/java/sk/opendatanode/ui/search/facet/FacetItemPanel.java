package sk.opendatanode.ui.search.facet;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sk.opendatanode.facet.FacetItem;
import sk.opendatanode.ui.HomePage;

public class FacetItemPanel extends Panel {

    private static final long serialVersionUID = 8720052701547361670L;
    private List<FacetItem> facetItems = new ArrayList<FacetItem>();
    private PageParameters parameters = null;

    public FacetItemPanel(String id, List<FacetItem> facetItem, PageParameters parameters) {
        super(id);
        
        this.parameters = parameters;
        add(new FacetItemListView("facetItemList", facetItems));
        
        facetItems.addAll(facetItem);
    }

    private class FacetItemListView extends ListView<FacetItem> {

        private static final long serialVersionUID = -8448695059163993780L;

        public FacetItemListView(String id, List<FacetItem> facetList) {
            super(id, facetList);
        }

        @Override
        protected void populateItem(ListItem<FacetItem> item) {
            FacetItem facetItem = item.getModelObject();
            PageParameters params = new PageParameters(parameters);
            
            facetItem.organizeParameters(params);
            
            BookmarkablePageLink<HomePage> link = new BookmarkablePageLink<HomePage>("facetItemUrl",
                                                                                     HomePage.class,
                                                                                     params);
            link.add(new Label("facetItemValue", facetItem.getName()));
            item.add(link);
            item.add(new Label("count", " ("+facetItem.getCount()+")"));
        }
        
    }
}
