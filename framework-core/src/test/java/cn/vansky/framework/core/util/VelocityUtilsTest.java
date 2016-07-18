package cn.vansky.framework.core.util;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class VelocityUtilsTest {
    @Test
    public void testParseFromString() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contractName", "2015政策");
        map.put("contractId", "1111");
        String content = VelocityUtils.getInstance().parse("contract_pass_notice.vm", map);
        System.out.println(content);
    }
}