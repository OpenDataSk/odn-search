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

package sk.opendatanode.utils.http;

import java.util.ArrayList;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class ContentNegotiablePage extends WebPage {

    private static final long serialVersionUID = 1L;
    
    public ContentNegotiablePage() {
        super();
        generateContentCheck(negotiateContent());
    }

    public ContentNegotiablePage(IModel<?> model) {
        super(model);
        generateContentCheck(negotiateContent());
    }

    public ContentNegotiablePage(PageParameters parameters) {
        super(parameters);
        generateContentCheck(negotiateContent());
    }

    /**
     * This method should define available content for implemented page.
     * @param contentTypes empty list
     * @return list of available content sorted by preference on server side
     */
    abstract public ArrayList<ContentTypes> defineAvailableContent(ArrayList<ContentTypes> contentTypes);

    /**
     * Handles content generation according to content type parsed form HTTP header
     * @param contentType 
     */
    abstract public void generateContent(ContentTypes contentType);

    private ContentTypes negotiateContent() {
        RequestCycle requestCycle = RequestCycle.get();
        ServletWebRequest request = (ServletWebRequest) requestCycle.getRequest();

        return ContentNegotiation.parseHttpContext(request.getHeader("Accept"),
                defineAvailableContent(new ArrayList<ContentTypes>()));
    }

    private void generateContentCheck(ContentTypes contentType) {
        if (contentType == null)
            throw new AbortWithHttpErrorCodeException(406);
        generateContent(contentType);
    }

}
