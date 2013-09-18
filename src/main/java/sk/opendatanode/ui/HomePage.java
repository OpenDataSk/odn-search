/* Copyright (C) 2012 Martin Virag <martin.virag@eea.sk>
 * Copyright (C) 2013 Tomas Matula <tomas.matula@eea.sk>
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

package sk.opendatanode.ui;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.opendatanode.ui.search.SearchResultPage;
import sk.opendatanode.utils.SolrQueryHelper;

/**
 * Homepage
 */
public class HomePage extends BasePage {
    private static final long serialVersionUID = -3362496726021637053L;
    private Logger logger = LoggerFactory.getLogger(HomePage.class);

    /**
     * Constructor that is invoked when page is invoked without a session.
     * 
     * @param params Page parameters
     */
    public HomePage(final PageParameters parameters) {
        super(parameters);

        QueryResponse response = null;
        try {
            response = SolrQueryHelper.search(parameters);
        } catch (IOException e) {
            logger.error("IOException error", e);
        } catch (SolrServerException e) {
            logger.error("SolrServerException", e);
        }

        SearchResultPage srp = new SearchResultPage("searchResultPage", parameters, response);
        add(srp);
    }
}
