package cn.vansky.framework.core.orm.mybatis.plugin.search.resolver;

import cn.vansky.framework.core.orm.mybatis.plugin.page.dialect.Dialect;
import cn.vansky.framework.core.orm.mybatis.plugin.search.vo.Param;
import cn.vansky.framework.core.orm.mybatis.plugin.search.vo.Searchable;
import cn.vansky.framework.core.orm.mybatis.plugin.search.vo.Sort;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Created by IntelliJ IDEA .
 * Auth: hyssop
 * Date: 2016/9/6 0006
 */
public abstract class AbstractSqlResolverInter implements SqlResolver {
    protected Param param = new Param();

    public AbstractSqlResolverInter(String alias){
        param.alias = alias;
        if (!StringUtils.isEmpty(alias)) {
            param.aliasWithDot = alias + ".";
        } else {
            param.aliasWithDot = "";
        }
    }
    public Param getParam(){
        return this.param;
    }


    public String getAliasWithDot() {
        return param.aliasWithDot;
    }
    public void prepareOrder(StringBuilder ql, Searchable search){
        if (search.hashSort()) {
            ql.append(" order by ");
            for (Sort.Order order : search.getSort()) {
                ql.append(String.format("%s%s %s, ", getAliasWithDot(), order.getProperty(), order.getDirection().name().toLowerCase()));
            }

            ql.delete(ql.length() - 2, ql.length());
        }
    }
    public void setPageable(StringBuilder sql, Searchable searchable , Dialect dialect){
        if (dialect.supportsLimit()&&searchable.getPage()!=null&& !ObjectUtils.equals(searchable.getPage(), ObjectUtils.NULL)) {
            int pageSize = searchable.getPage().getPageSize();
            int index = (searchable.getPage().getPageNumber() - 1) * pageSize;
            int start = index < 0 ? 0 : index;
            dialect.getLimitString(sql, start, pageSize);
        }
    }
}
