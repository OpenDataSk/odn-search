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

package sk.opendatanode.facet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.junit.Test;

public class FacetItemTest {

    @Test
    public void testGetQueryFieldName() {
        assertEquals(null,FacetItem.getQueryFieldName(null));
        assertEquals(null, FacetItem.getQueryFieldName(""));
        assertEquals("type", FacetItem.getQueryFieldName("type:ORGANIZATION_RECORD"));
        assertEquals("donation_value", FacetItem.getQueryFieldName("donation_value:[0.01 TO 999.99]"));
        assertEquals("currency", FacetItem.getQueryFieldName("-currency:(EUR SKK)"));
    }
    
    @Test
    public void testOrganizeParameters() {
        FacetItem itemCurrencySKK = new FacetItem("SKK", "currency:SKK", 0);
        FacetItem itemCurrencyIne = new FacetItem("ine", "-currency:(EUR SKK)", 0);
        FacetItem itemPrice = new FacetItem("100 000 - 1 000 000", "price:[100000 TO 999999.99]", 0);
        FacetItem itemOR = new FacetItem("organizacia", "type:ORGANIZATION_RECORD", 0);
        FacetItem itemPR = new FacetItem("obstaravanie", "type:PRECUREMENT_RECORD", 0);
        
        PageParameters par = new PageParameters();
        
        // test na: nema ziaden filter = zadal dataset
        itemOR.organizeParameters(par);
        assertEquals(itemOR.getQuery(), par.get("fq").toString());
        
        // test na: ma iba jeden (len pre type)        
        //      click na facet s rovnakym typom / dvojclick
        itemOR.organizeParameters(par);
        assertTrue(par.get("fq").isNull());
//        assertEquals(null, par.get("fq"));
        //      click na facet s inym typom / iny dataset
        itemPR.organizeParameters(par);
        assertEquals(itemPR.getQuery(), par.get("fq").toString());
        
        // test na: ma viac filtrov
        itemCurrencySKK.organizeParameters(par);
        System.out.println(par);
        List<StringValue> list = par.getValues("fq");
        assertTrue(contains(list,itemPR.getQuery()));
        assertTrue(contains(list,itemCurrencySKK.getQuery()));
        //      pridavam odlisny
        itemPrice.organizeParameters(par);
        System.out.println(par);
        list = par.getValues("fq");
        assertTrue(contains(list, itemPR.getQuery()));
        assertTrue(contains(list,itemCurrencySKK.getQuery()));
        assertTrue(contains(list,itemPrice.getQuery()));
        //      dvojclick
        itemPrice.organizeParameters(par);
        System.out.println(par);
        list = par.getValues("fq");
        assertTrue(contains(list,itemPR.getQuery()));
        assertTrue(contains(list,itemCurrencySKK.getQuery()));
        assertTrue(!contains(list,itemPrice.getQuery()));

        //      click na facet s rovnakou "kategoriou"
        itemCurrencyIne.organizeParameters(par);
        System.out.println(par);
        list = par.getValues("fq");
        assertTrue(contains(list,itemPR.getQuery()));
        assertTrue(contains(list,itemCurrencyIne.getQuery()));
        
        //      dvojclick na OR/PR ked uz mam pridane aj podkategorie
        itemOR.organizeParameters(par);
        System.out.println(par);
        list = par.getValues("fq");
        assertTrue(contains(list,itemOR.getQuery()));
        assertTrue(!contains(list,itemCurrencyIne.getQuery()));
    }

    private boolean contains(List<StringValue> list, String query) {
        return list.contains(StringValue.valueOf(query));
    }
}
