package cn.vansky.framework.common.util;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PrimitiveJavaTypeTest {

    @Test
    public void testCovertValue() throws Exception {
        Object obj = PrimitiveJavaType.covertValue(Integer.class, "11");
        Assert.assertEquals(obj, 11);
    }
}