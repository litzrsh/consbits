package me.litzrsh.consbits.core.util;


import me.litzrsh.consbits.core.Tree;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused"})
public abstract class TreeUtils {

    public static <T extends Tree<T>> List<T> convert(List<T> nodes) {
        return convert(nodes, false);
    }

    public static <T extends Tree<T>> List<T> convert(List<T> nodes, boolean noRoot) {
        List<T> roots = nodes.stream()
                .filter(
                        node -> node.getParentId() == null ||
                                (noRoot && nodes.stream().noneMatch(n -> n.getId().equals(node.getParentId())))
                )
                .sorted()
                .toList();
        for (T root : roots) {
            root.setChildren(getChildren(root, nodes));
        }
        return roots;
    }

    protected static <T extends Tree<T>> List<T> getChildren(T parent, List<T> nodes) {
        List<T> children = nodes.stream().filter(node -> parent.getId().equals(node.getParentId())).toList();
        if (CollectionUtils.isEmpty(children)) return null;
        for (T child : children) {
            child.setChildren(getChildren(child, nodes));
        }
        return children;
    }

    public static <T extends Tree<T>> List<T> flatten(List<T> nodes) {
        List<T> array = new ArrayList<>(nodes);
        array.forEach((node) -> appendChildren(array, node.getChildren()));
        return array;
    }

    protected static <T extends Tree<T>> void appendChildren(List<T> array, List<T> nodes) {
        if (CollectionUtils.isEmpty(nodes)) return;
        array.addAll(nodes);
        nodes.forEach(node -> appendChildren(array, node.getChildren()));
    }
}
