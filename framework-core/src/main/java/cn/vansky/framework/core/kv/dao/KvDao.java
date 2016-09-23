/*
 * Copyright (C) 2016 CK, Inc. All Rights Reserved.
 */

package cn.vansky.framework.core.kv.dao;

import java.util.List;
import java.util.Map;

/**
 * The query result that the SQL of the database.
 * the {@linkplain cn.vansky.framework.core.kv.dao.impl.KvDaoImpl#dataSource}
 * is setted through the XML injection of the spring.
 * <pre>
 * &lt;bean id="kvDao" class="cn.vansky.framework.core.kv.dao.impl.KvDaoImpl"&gt;
 *     &lt;property name="dataSource" ref="dataSource"/&gt;
 * &lt;/bean&gt;
 * </pre>
 * Author: CK
 * Date: 2015/11/13
 */
public interface KvDao {

    /**
     * The method that get the result of the database
     *
     * @param sql the sql
     * @param namedParams the named params
     * @return the new {@code List<Map>}
     */
    public List<Map<String, Object>> execute(String sql, Map<String, Object> namedParams);
}
