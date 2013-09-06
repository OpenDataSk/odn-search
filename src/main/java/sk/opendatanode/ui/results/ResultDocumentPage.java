/* Copyright (C) 2013 Tomas Matula <tomas.matula@eea.sk>
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

package sk.opendatanode.ui.results;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.opendatanode.solr.SolrServerRep;
import sk.opendatanode.ui.search.SearchQueryPage;
import sk.opendatanode.utils.http.ContentNegotiablePage;
import sk.opendatanode.utils.http.ContentTypes;

import com.google.gson.Gson;

public class ResultDocumentPage extends ContentNegotiablePage {

    private static final long serialVersionUID = 1L;
    private Logger logger;
    
    @Override
    public ArrayList<ContentTypes> defineAvailableContent(ArrayList<ContentTypes> contentTypes) {
        contentTypes.add(ContentTypes.JSON);
        contentTypes.add(ContentTypes.HTML);
        return contentTypes;
    }

    @Override
    public void generateContent(ContentTypes contentType) {

        logger = LoggerFactory.getLogger(ResultDocumentPage.class);

        // Search panel
        add(new SearchQueryPage("searchPage", getPageParameters()));

        // param represents id for search defined in URL
        String param = "";
        String url = ((WebRequest) RequestCycle.get().getRequest()).getUrl().toString();
        
        if (url.startsWith("item/")) {
            int paramIndex = url.indexOf("?");
            if (paramIndex != -1) {
                param = url.substring("item/".length(), paramIndex);
                
            } else {
                param = url.substring("item/".length());
            }
        }
        
        // Default value for no results
        Panel resultPanel = new EmptyPanel("resultPanel");
        Label messageLabel = new Label("messageLabel", "");

        if ("".equals(param)) {
            messageLabel = new Label("messageLabel", "No results was found");
        } else
            try {
                // Solr request to db
                List<SolrDocument> resultList = SolrServerRep.getInstance().sendQuery(new SolrQuery("id:" + param))
                        .getResults();

                switch (contentType) {
                    case HTML: // render as HTML
                        switch (resultList.size()) { // behave according to result count
                            case 0:
                                logger.info("No results for id " + param);
                                messageLabel = new Label("messageLabel", "No results was found");
                                break;
                            case 1:
                                resultPanel = new GenericResultPanel("resultPanel", resultList.get(0));
                                break;
                            default:
                                logger.info("Multiple results for id " + param);
                                messageLabel = new Label("messageLabel",
                                        "Error: multiple results found for specified id");
                        }
                        break;
                    case JSON: // render as JSON
                        Gson gson = new Gson();
                        RequestCycle.get()
                                .scheduleRequestHandlerAfterCurrent(
                                        (new TextRequestHandler(ContentTypes.JSON.getLabel(), "utf-8", gson
                                                .toJson(resultList))));
                        break;
                    default:
                        // Unspecified content
                        throw new AbortWithHttpErrorCodeException(406);
                }
            } catch (SolrServerException e) {
                logger.error("SolrServerException", e);
            } catch (IOException e) {
                logger.error("IOException error", e);
            }

        add(resultPanel);
        add(messageLabel);
    }
    
}
