package me.litzrsh.consbits.core;

import java.util.List;

public interface Tree<T> extends Comparable<Tree<T>> {

    String getId();

    String getParentId();

    Integer getSort();

    List<T> getChildren();

    void setChildren(List<T> children);

    @Override
    default int compareTo(Tree<T> o) {
        return getSort() - o.getSort();
    }
}
