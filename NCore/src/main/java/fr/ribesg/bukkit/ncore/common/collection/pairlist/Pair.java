/***************************************************************************
 * Project file:    NPlugins - NCore - Pair.java                           *
 * Full Class name: fr.ribesg.bukkit.ncore.common.collection.pairlist.Pair *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common.collection.pairlist;

public class Pair<K, V> {

    private K key;
    private V value;

    public Pair(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public void setKey(final K key) {
        this.key = key;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(final V value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        final Pair pair = (Pair)o;

        return !(this.key != null ? !this.key.equals(pair.key) : pair.key != null) && !(this.value != null ? !this.value.equals(pair.value) : pair.value != null);
    }

    @Override
    public int hashCode() {
        int result = this.key != null ? this.key.hashCode() : 0;
        result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pair{" +
               "key=" + this.key +
               ", value=" + this.value +
               '}';
    }
}
