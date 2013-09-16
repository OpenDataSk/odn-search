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
import java.util.Map;

import org.apache.wicket.Application;
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

public class FacetPanel extends Panel{
    
    private static final long serialVersionUID = 4635123246467080919L;
    private List<FacetInfo> facetInfoList = new ArrayList<FacetInfo>();
    private PageParameters parameters = null;
    private boolean stripTags;
    
    public FacetPanel(String id, PageParameters params, Map<String, Integer> facetItems) {
        super(id);
        
        stripTags =  Application.get().getMarkupSettings().getStripWicketTags(); //no wicket tags in this panel
        
        this.parameters = params;
        selectFacetCategories(params, facetItems);
        add(new FacetListView("facetList", facetInfoList));
    }

    /**
     * putting together of facets for each type
     * @param params
     * @param facetItems
     */
    private void selectFacetCategories(PageParameters params, Map<String, Integer> facetItems) {
        boolean all = SolrQueryHelper.hasTypeAll(params);
        if(all || SolrQueryHelper.hasType(SolrType.ORGANIZATION, params)) {
            add(FacetItemType.LEGAL_FORM, facetItems);
            add(FacetItemType.SEAT, facetItems);
            add(FacetItemType.DATE_FROM, facetItems);
            add(FacetItemType.DATE_TO, facetItems);
        }
        if (all || SolrQueryHelper.hasType(SolrType.PROCUREMENT, params)) {
            add(FacetItemType.YEAR, facetItems);
            add(FacetItemType.PRICE, facetItems);
            add(FacetItemType.CURRENCY, facetItems);
            add(FacetItemType.VAT, facetItems);
        }
        if(all || SolrQueryHelper.hasType(SolrType.POLITICAL_PARTY_DONATION, params)) {
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
            item.setRenderBodyOnly(true);
        }
        
    }
    
    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        Application.get().getMarkupSettings().setStripWicketTags(true);
    }
    @Override
    protected void onAfterRender() {
        super.onAfterRender();
        Application.get().getMarkupSettings().setStripWicketTags(stripTags);
    }
}
