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

public class ContentTreeNode<E> {

    private E value;

    private ContentTreeNode<E> parent;
    private ArrayList<ContentTreeNode<E>> children;

    public ContentTreeNode() {
        parent = null;
        children = new ArrayList<ContentTreeNode<E>>();
    }

    public ContentTreeNode(ContentTreeNode<E> parent) {
        this.parent = parent;
        parent.addChild(this);
        children = new ArrayList<ContentTreeNode<E>>();
    }

    public ContentTreeNode(ContentTreeNode<E> parent, E value) {
        this(parent);
        this.value = value;
    }

    // For search purposes only
    public ContentTreeNode(E value) {
        this.value = value;
    }

    public ContentTreeNode<E> getParent() {
        return parent;
    }

    public ArrayList<ContentTreeNode<E>> getChildren() {
        return children;
    }

    public void addChild(ContentTreeNode<E> child) {
        children.add(child);
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContentTreeNode<?> other = (ContentTreeNode<?>) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
