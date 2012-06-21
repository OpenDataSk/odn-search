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
