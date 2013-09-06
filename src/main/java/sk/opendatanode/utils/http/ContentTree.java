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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContentTree {

    private class ContentTreeRoot extends ContentTreeNode<String> {

        private Float quality;

        public ContentTreeRoot(Float quality) {
            super();
            this.quality = quality;
            this.setValue(new DecimalFormat("#.##").format(quality));
        }

        public Float getQuality() {
            return quality;
        }

        @Override
        public String toString() {
            return "ContentTreeRoot [quality=" + quality + "]";
        }

    }

    private ArrayList<ContentTreeRoot> roots;

    public ContentTree() {
        roots = new ArrayList<ContentTreeRoot>();
    }

    public void addContent(String type, String subtype, Float quality) {
        ContentTreeRoot root = new ContentTreeRoot(quality);
        ContentTreeNode<String> node;

        int index = roots.indexOf(root);

        if (index == -1) {
            roots.add(root);
        } else {
            root = roots.get(index);
        }

        index = root.getChildren().indexOf(new ContentTreeNode<String>(type));

        if (index == -1) {
            node = new ContentTreeNode<String>(root, type);
        } else {
            node = root.getChildren().get(index);
        }

        if (!node.getChildren().contains(new ContentTreeNode<String>(subtype))) {
            node = new ContentTreeNode<String>(node, subtype);
        }
    }

    public ContentTypes searchFromRoots(ContentTreeNode<String> availableContent) {
        sortRoots();

        ContentTreeNode<String> leaf;

        for (ContentTreeRoot root : roots) {

            // Search for available content in request type
            for (ContentTreeNode<String> avalCont : availableContent.getChildren()) {
                leaf = null;
                int index = root.getChildren().indexOf(avalCont);
                if (index != -1) {
                    leaf = matchedType(root.getChildren().get(index), avalCont);
                } else {
                    index = root.getChildren().indexOf(new ContentTreeNode<String>("*"));
                    if (index != -1) {
                        leaf = matchedType(root.getChildren().get(index), avalCont);
                    }
                }
                if (leaf != null) {
                    return buildContentType(leaf);
                }
            }
        }
        return null;
    }

    public ContentTreeNode<String> getFirstRoot() {
        if (roots.size() > 0) {
            return roots.get(0);
        }

        return null;
    }

    private ContentTreeNode<String> matchedType(ContentTreeNode<String> node, ContentTreeNode<String> availableContent) {

        for (ContentTreeNode<String> avalCont : availableContent.getChildren()) {
            if (node.getChildren().contains(avalCont)) {
                return avalCont;
            } else {
                if (node.getChildren().contains(new ContentTreeNode<String>("*"))) {
                    return avalCont;
                }
            }
        }

        return null;
    }

    private ContentTypes buildContentType(ContentTreeNode<String> node) {
        String cType = node.getParent().getValue() + "/" + node.getValue();
        for (ContentTypes type : ContentTypes.values()) {
            if (type.getLabel().equals(cType)) {
                return type;
            }
        }
        return null;
    }

    private void sortRoots() {
        Collections.sort(roots, new Comparator<ContentTreeRoot>() {

            public int compare(ContentTreeRoot o1, ContentTreeRoot o2) {
                return Float.compare(o2.getQuality(), o1.getQuality());
            }
        });

    }

}
