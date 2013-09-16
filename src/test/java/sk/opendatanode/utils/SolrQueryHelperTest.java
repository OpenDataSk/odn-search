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

package sk.opendatanode.utils;

import static org.junit.Assert.assertTrue;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.junit.Test;

import sk.opendatanode.solr.SolrType;

public class SolrQueryHelperTest {

    @Test
    public void testHasType() {
        PageParameters p = new PageParameters();
        assertTrue(!SolrQueryHelper.hasType(SolrType.ORGANIZATION, p));
        
        p.set("fq", StringValue.valueOf("type:ORG*"));
        assertTrue(SolrQueryHelper.hasType(SolrType.ORGANIZATION, p));
    }
    
    @Test
    public void testGetFacetDateRangeQuery() {
        String dateRange = SolrQueryHelper.getFacetDateRangeQuery("date_from", 2000, 2005);
        //date_from:[1999-12-31T23:00:00.000Z+TO+2004-12-31T22:59:59.999Z]
        
        String datePattern = "\\d{4}\\-[0-2]\\d\\-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d\\.\\d{3}Z";
        String rangePattern = "\\["+datePattern+" TO "+datePattern+"\\]";
        assertTrue(dateRange.matches("date_from:"+rangePattern));
    }
}
