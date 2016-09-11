/*
 * Copyright (C) 2016 hyssop, Inc. All Rights Reserved.
 */

package cn.vansky.framework.tree.model.easyui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by framework .
 * Auth: hyssop
 * Date: 2016-09-09
 */
public class SimpleEasyUiTree<T extends EasyUiTree, ID> extends EasyUiTree {
    private List<T> children;

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public void addChildren(T model) {
        if (null == children) {
            children = new ArrayList<T>();
        }
        children.add(model);
    }
}
