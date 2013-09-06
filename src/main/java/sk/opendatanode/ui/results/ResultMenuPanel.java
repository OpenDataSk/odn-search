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

import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;

public class ResultMenuPanel extends Panel {

    private static final long serialVersionUID = 1L;
    
    public ResultMenuPanel(String id) {
        super(id);

        ServletWebRequest request = (ServletWebRequest) RequestCycle.get().getRequest();
        String referrer = request.getHeader("referer");
        
        if(referrer != null && referrer.startsWith(request.getUrl().getProtocol() + "://" + request.getUrl().getHost())) {
	        add(new ExternalLink("backLink",
	        		  "javascript:history.go(-1)", 
	        		  getString("back")));
        } else {
        	add(new ExternalLink("backLink", "../", getString("back")));
        }
    }
    
}
