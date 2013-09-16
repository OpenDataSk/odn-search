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

package sk.opendatanode.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.opendatanode.model.AbstractRecord;
import sk.opendatanode.model.OrganizationRecord;
import sk.opendatanode.model.PoliticalPartyDonationRecord;
import sk.opendatanode.model.ProcurementRecord;
import sk.opendatanode.solr.SolrType;

public class SolrFactory {

    private static Logger logger = LoggerFactory.getLogger(SolrFactory.class);

    /**
     * Converts solr document into record according to solr document type property
     * 
     * @param solrDocument output entry of solr response
     * @return abstract representation of record
     */
    public static AbstractRecord sorlToRecord(SolrDocument solrDocument) {
        AbstractRecord record = null;

        String type = (String) solrDocument.get("type");

        if (type == null)
            return null;

        if (type.equals(SolrType.ORGANIZATION.getTypeString())) {
            record = new OrganizationRecord();
        } else if (type.equals(SolrType.POLITICAL_PARTY_DONATION.getTypeString())) {
            record = new PoliticalPartyDonationRecord();
        } else if (type.equals(SolrType.PROCUREMENT.getTypeString())) {
            record = new ProcurementRecord();
        }

        try {
            BeanUtils.copyProperties(record, solrDocument);
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException", e);
        } catch (InvocationTargetException e) {
            logger.error("InvocationTargetException", e);
        }

        return record;
    }

    /**
     * Converts solr document into map with keys obtained form record object specified by type property of solr document
     * 
     * @param solrDocument output entry of solr response
     * @return map of objects
     */
    public static Map<String, Object> solrToMap(SolrDocument solrDocument) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        String type = (String) solrDocument.get("type");

        if (type == null)
            return null;

        Field[] fields = new Field[0];
        if (type.equals(SolrType.ORGANIZATION.getTypeString())) {
            fields = OrganizationRecord.class.getDeclaredFields();
        } else if (type.equals(SolrType.POLITICAL_PARTY_DONATION.getTypeString())) {
            fields = PoliticalPartyDonationRecord.class.getDeclaredFields();
        } else if (type.equals(SolrType.PROCUREMENT.getTypeString())) {
            fields = ProcurementRecord.class.getDeclaredFields();
        }

        String name;
        for (Field field : fields) {
            name = field.getName().replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();
            map.put(name, solrDocument.get(name));
        }

        return map;
    }
}
