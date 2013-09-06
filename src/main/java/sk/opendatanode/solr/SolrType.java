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

package sk.opendatanode.solr;

public enum SolrType {
    ORGANIZATION,
    PROCUREMENT,
    POLITICAL_PARTY_DONATION,
    ALL;
    
    public String getTypeString() {
        switch (this) {
            case ORGANIZATION:
                return "ORGANIZATION_RECORD";
            case PROCUREMENT:
                return "PROCUREMENT_RECORD";
            case POLITICAL_PARTY_DONATION:
                return "POLITICAL_PARTY_DONATION_RECORD";
            default:
                return null;
        }
    }
    
    public String getReadableString() {
        switch (this) {
            case ORGANIZATION:
                return "Organizácie";
            case PROCUREMENT:
                return "Obstarávanie";
            case POLITICAL_PARTY_DONATION:
                return "Sponzori politických strán";
            default:
                return "Prehladať všetky datasety";
        }
    }
    
    public static SolrType getTypeFromReadableString(String text) {
        if (text == null || text.isEmpty()) {
            return null;            
        }
        
        if(text.equals(ORGANIZATION.getReadableString()))
            return ORGANIZATION;
        else if(text.equals(PROCUREMENT.getReadableString()))
            return PROCUREMENT;
        else if(text.equals(POLITICAL_PARTY_DONATION.getReadableString()))
            return POLITICAL_PARTY_DONATION;
        else if(text.equals(ALL.getReadableString()))
            return ALL;
        
        return null;
    }
}
