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

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

import sk.opendatanode.ui.results.ResultDocumentPage;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see wicket.myproject.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{    
    /**
     * Constructor
     */
	public WicketApplication() {
	}
	
	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

    @Override
    protected void init() {
        super.init();
        mountPage("/item", ResultDocumentPage.class);
    }
	
	

}
