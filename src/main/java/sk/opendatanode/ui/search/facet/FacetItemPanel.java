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
    private static final long serialVersionUID = 32353931182742617L;
    private List<FacetItem> facetItems = new ArrayList<FacetItem>();
    private PageParameters parameters = null;

    public FacetItemPanel(String id, List<FacetItem> facetItem, PageParameters parameters) {
        super(id);
        
        this.parameters = parameters;
        facetItems.addAll(facetItem);
        add(new FacetItemListView("facetItemList", facetItems));
        
        setRenderBodyOnly(true); // not rendering span
    }

    private class FacetItemListView extends ListView<FacetItem> {
        private static final long serialVersionUID = -7606331789314026088L;

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
            link.setEnabled(facetItem.getCount()!=0);
            link.add(new Label("facetItemValue", facetItem.getName()));
            item.add(link);
            item.add(new Label("count", " ("+facetItem.getCount()+")"));
        }
        
    }
}
