/*
 * Copyright (C) 2016 hyssop, Inc. All Rights Reserved.
 */

package cn.vansky.framework.tree.service;

import cn.vansky.framework.core.dao.SqlMapDao;
import cn.vansky.framework.core.orm.mybatis.plugin.search.enums.SearchOperator;
import cn.vansky.framework.core.orm.mybatis.plugin.search.filter.CustomConditionFactory;
import cn.vansky.framework.core.orm.mybatis.plugin.search.filter.SearchFilter;
import cn.vansky.framework.core.orm.mybatis.plugin.search.vo.Searchable;
import cn.vansky.framework.core.service.GenericSqlMapServiceImpl;
import cn.vansky.framework.tree.bo.Treeable;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by framework-tree.
 * Auth: hyssop
 * Date: 2016/9/7
 */
public abstract class BaseTreeableServiceImpl<T extends Treeable<ID>, ID extends Serializable>
        extends GenericSqlMapServiceImpl {

    public abstract SqlMapDao<T, ID> getDao();

    public T save(T m) {
        if (m.getWeight() == null) {
            m.setWeight(nextWeight(m.getParentId()));
        }
        getDao().saveOrUpdate(m);
        return m;
    }

    @Transactional
    public void deleteSelfAndChild(T m) {
        List<T> ms = getDao().findAll();
        List<T> results = new ArrayList<T>();
        for (T t : ms) {
            if (t.getParentId().equals(m.getId())) {
                getDao().delete((ID) t.getId());
            }
        }
        getDao().delete((ID) m.getId());
    }

    public void deleteSelfAndChild(List<T> mList) {
        for (T m : mList) {
            deleteSelfAndChild(m);
        }
    }

    public void appendChild(T parent, T child) {
        child.setParentId((ID) parent.getId());
        child.setParentIds(parent.makeSelfAsNewParentIds());
        child.setWeight(nextWeight((ID) parent.getId()));
        save(child);
    }

    public int nextWeight(ID id) {
        return getDao().findById(id).getWeight();
    }

    /**
     * 移动节点
     * 根节点不能移动
     *
     * @param source   源节点
     * @param target   目标节点
     * @param moveType 位置
     */
    public void move(T source, T target, String moveType) {
        if (source == null || target == null || source.isRoot()) { //根节点不能移动
            return;
        }

        //如果是相邻的兄弟 直接交换weight即可
        boolean isSibling = source.getParentId().equals(target.getParentId());
        boolean isNextOrPrevMoveType = "next".equals(moveType) || "prev".equals(moveType);
        if (isSibling && isNextOrPrevMoveType && Math.abs(source.getWeight() - target.getWeight()) == 1) {
            //无需移动
            if ("next".equals(moveType) && source.getWeight() > target.getWeight()) {
                return;
            }
            if ("prev".equals(moveType) && source.getWeight() < target.getWeight()) {
                return;
            }
            int sourceWeight = source.getWeight();
            source.setWeight(target.getWeight());
            target.setWeight(sourceWeight);
            return;
        }

        //移动到目标节点之后
        if ("next".equals(moveType)) {
            List<T> siblings = findSelfAndNextSiblings(target.getParentIds(), target.getWeight());
            siblings.remove(0);//把自己移除

            if (siblings.size() == 0) { //如果没有兄弟了 则直接把源的设置为目标即可
                int nextWeight = nextWeight(target.getParentId());
                updateSelftAndChild(source, target.getParentId(), target.getParentIds(), nextWeight);
                return;
            } else {
                moveType = "prev";
                target = siblings.get(0); //否则，相当于插入到实际目标节点下一个节点之前
            }
        }

        //移动到目标节点之前
        if ("prev".equals(moveType)) {

            List<T> siblings = findSelfAndNextSiblings(target.getParentIds(), target.getWeight());
            //兄弟节点中包含源节点
            if (siblings.contains(source)) {
                // 1 2 [3 source] 4
                siblings = siblings.subList(0, siblings.indexOf(source) + 1);
                int firstWeight = siblings.get(0).getWeight();
                for (int i = 0; i < siblings.size() - 1; i++) {
                    siblings.get(i).setWeight(siblings.get(i + 1).getWeight());
                }
                siblings.get(siblings.size() - 1).setWeight(firstWeight);
            } else {
                // 1 2 3 4  [5 new]
                int nextWeight = nextWeight(target.getParentId());
                int firstWeight = siblings.get(0).getWeight();
                for (int i = 0; i < siblings.size() - 1; i++) {
                    siblings.get(i).setWeight(siblings.get(i + 1).getWeight());
                }
                siblings.get(siblings.size() - 1).setWeight(nextWeight);
                source.setWeight(firstWeight);
                updateSelftAndChild(source, target.getParentId(), target.getParentIds(), source.getWeight());
            }

            return;
        }
        //否则作为最后孩子节点
        int nextWeight = nextWeight((ID) target.getId());
        updateSelftAndChild(source, (ID) target.getId(), target.makeSelfAsNewParentIds(), nextWeight);
    }

    /**
     * 把源节点全部变更为目标节点
     *
     * @param source
     * @param newParentIds
     */
    public void updateSelftAndChild(T source, ID newParentId, String newParentIds, int newWeight) {
        String oldSourceChildrenParentIds = source.makeSelfAsNewParentIds();
        source.setParentId(newParentId);
        source.setParentIds(newParentIds);
        source.setWeight(newWeight);
        getDao().saveOrUpdate(source);
        String newSourceChildrenParentIds = source.makeSelfAsNewParentIds();
    }

    /**
     * 查找目标节点及之后的兄弟  注意：值与越小 越排在前边
     *
     * @param parentIds
     * @param currentWeight
     * @return
     */
    public List<T> findSelfAndNextSiblings(String parentIds, int currentWeight) {
        List<T> result = new ArrayList<T>();
        List<T> all = getDao().findAll();
        for (T part : all) {
            if (part.getParentIds().equalsIgnoreCase(parentIds) && part.getWeight() == currentWeight) {
                result.add(part);
            }
        }
        return result;
    }

    /**
     * 查看与name模糊匹配的名称
     *
     * @param name
     * @return
     */
    public Set<String> findNames(Searchable searchable, String name, ID excludeId) throws InvocationTargetException, IllegalAccessException {
        T excludeM = getDao().findById(excludeId);

        searchable.addSearchFilter("name", SearchOperator.like, name);
        addExcludeSearchFilter(searchable, excludeM);

        return Sets.newHashSet(
                Lists.transform(
                        findBySearchable(searchable).getContent(),
                        new Function<T, String>() {
                            public String apply(T input) {
                                return input.getName();
                            }
                        }
                )
        );
    }

    /**
     * 查询子子孙孙
     *
     * @return
     */
    public List<T> findChildren(List<T> parents, Searchable searchable)
            throws InvocationTargetException, IllegalAccessException {
        if (parents.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        SearchFilter first = CustomConditionFactory.newCustomCondition("parent_ids",
                SearchOperator.prefixLike,
                parents.get(0).makeSelfAsNewParentIds());
        SearchFilter[] others = new SearchFilter[parents.size() - 1];
        for (int i = 1; i < parents.size(); i++) {
            others[i - 1] = CustomConditionFactory.newCustomCondition("parent_ids",
                    SearchOperator.prefixLike,
                    parents.get(i).makeSelfAsNewParentIds());
        }
        searchable.or(first, others);

        List children = getDao().findBySearchable(searchable).getContent();
        return children;
    }

    public List<T> findAllByName(Searchable searchable, T excludeM)
            throws InvocationTargetException, IllegalAccessException {
        addExcludeSearchFilter(searchable, excludeM);
        List list = getDao().findBySearchable(searchable).getContent();
        return list;
    }

    /**
     * 查找根和一级节点
     *
     * @param searchable
     * @return
     */
    public List<T> findRootAndChild(Searchable searchable) throws InvocationTargetException, IllegalAccessException {
        searchable.addSearchParam("parent_id_eq", 0);
        List<T> models = getDao().findBySearchableForTree(searchable);
        if (models.size() == 0) {
            return models;
        }
        List<String> ids = Lists.newArrayList();
        for (int i = 0; i < models.size(); i++) {
            ids.add(String.valueOf(models.get(i).getId()));
        }
        searchable.removeSearchFilter("parent_id_eq");
        String[] array = ids.toArray(new String[ids.size()]);
        searchable.addSearchParam("parent_id_in", array);
        models.addAll(getDao().findBySearchableForTree(searchable));
        return models;
    }

    public Set<ID> findAncestorIds(Iterable<ID> currentIds) {
        Set<ID> parents = Sets.newHashSet();
        for (ID currentId : currentIds) {
            parents.addAll(findAncestorIds(currentId));
        }
        return parents;
    }

    public Set<ID> findAncestorIds(ID currentId) {
        Set ids = Sets.newHashSet();
        T m = getDao().findById(currentId);
        if (m == null) {
            return ids;
        }
        for (String idStr : StringUtils.tokenizeToStringArray(m.getParentIds(), "/")) {
            if (!StringUtils.isEmpty(idStr)) {
                ids.add(Long.valueOf(idStr));
            }
        }
        return ids;
    }

    /**
     * 递归查询祖先
     *
     * @param parentIds
     * @return
     */
    public List<T> findAncestor(String parentIds) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isEmpty(parentIds)) {
            return Collections.EMPTY_LIST;
        }
        String[] ids = StringUtils.tokenizeToStringArray(parentIds, "/");
        List results = (List) getDao().findBySearchable(Searchable.newSearchable().addSearchFilter("id", SearchOperator.in, ids));

        return results;
    }

    public void addExcludeSearchFilter(Searchable searchable, T excludeM) {
        if (excludeM == null) {
            return;
        }
        searchable.addSearchFilter("id", SearchOperator.ne, excludeM.getId());
        searchable.addSearchFilter("parent_ids", SearchOperator.suffixNotLike, excludeM.makeSelfAsNewParentIds());
    }
}
