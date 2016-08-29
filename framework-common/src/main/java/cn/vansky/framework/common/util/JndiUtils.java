/*
 * Copyright (C) 2016 CK, Inc. All Rights Reserved.
 */

package cn.vansky.framework.common.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * A utility class for Jndi operation.
 * Auth: CK
 * Date: 2016/8/29
 */
public class JndiUtils {
    /**
     * jave env prefix
     */
    public static final String JNDI_PREFIX = "java:comp/env";

    /**
     * get value by key from namng service
     *
     * @param key name
     * @return value
     * @throws NamingException in case of connect naming service failed.
     */
    public static String getValue(String key) throws NamingException {
        Context ctx = new InitialContext();
        return (String) ctx.lookup(key);
    }
}
