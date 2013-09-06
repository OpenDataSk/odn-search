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

package sk.opendatanode.ui.search;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sk.opendatanode.solr.SolrType;
import sk.opendatanode.utils.SolrQueryHelper;


public class SearchQueryPage extends Panel{

    private static final long serialVersionUID = 9058238555708124775L;
    private String query = null;
    private String selected = SolrType.ORGANIZATION.getReadableString();

    public SearchQueryPage(String id, PageParameters params) {
        super(id);
        this.query  = params.get("q").toString("").trim();
        setSelected(params);
        
        SearchForm form = new SearchForm("searchForm");
        
        form.add(new Label("selected", selected));
        form.add(new Label("defaultValue", selected));
        form.add(new Label("all", SolrType.ALL.getReadableString()));
        form.add(new Label("org", SolrType.ORGANIZATION.getReadableString()));
        form.add(new Label("obs", SolrType.PROCUREMENT.getReadableString()));
        form.add(new Label("spo", SolrType.POLITICAL_PARTY_DONATION.getReadableString()));
        
        form.add(new TextField<String>("query", new PropertyModel<String>(this, "query")));
        
        add(form);
    }

    private void setSelected(PageParameters params) {
        if(SolrQueryHelper.hasType(SolrType.ORGANIZATION, params))
            selected = SolrType.ORGANIZATION.getReadableString();
        else if(SolrQueryHelper.hasType(SolrType.PROCUREMENT, params))
            selected = SolrType.PROCUREMENT.getReadableString();
        else if(SolrQueryHelper.hasType(SolrType.POLITICAL_PARTY_DONATION, params))
            selected = SolrType.POLITICAL_PARTY_DONATION.getReadableString();
        else
            selected = SolrType.ALL.getReadableString();
    }

    private final class SearchForm extends Form<Void> {

        private static final long serialVersionUID = 7442088284418137882L;

        public SearchForm(String id) {
            super(id);
        }
        
        @Override
        protected void onSubmit() {
            
            PageParameters params = new PageParameters();
            
            if (query != null && !query.isEmpty())
                params.add("q", query);
            
            if(getSelectedType() != null)
                params.add("fq", "type:"+getSelectedType()+"*");
            
            setResponsePage(getApplication().getHomePage(), params);
        }

        private String getSelectedType() {
            selected = getRequest().getRequestParameters().getParameterValue("selected")
                    .toString(SolrType.ORGANIZATION.getReadableString());
            
            if(selected.equals(SolrType.ALL.getReadableString()))
                return null;
            String type = SolrType.getTypeFromReadableString(selected).getTypeString();
            return type.substring(0, 3);
        }
    }
}
