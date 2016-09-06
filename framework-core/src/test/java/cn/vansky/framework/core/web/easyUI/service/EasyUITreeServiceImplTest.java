package cn.vansky.framework.core.web.easyUI.service;

import cn.vansky.framework.core.web.easyUI.model.EasyUITreeModel;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class EasyUITreeServiceImplTest {
    @InjectMocks
    EasyUITreeService<Param> service;

    @BeforeClass(alwaysRun = true)
    public void initMocks() {
        service = new EasyUITreeServiceImpl<Param>();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindModel() throws Exception {
        String json = service.findTreeStr(getList(), new EasyUITreeService.ModelCall<Param>() {
            public EasyUITreeModel convert(Param p) {
                EasyUITreeModel m = new EasyUITreeModel();
                m.setId(p.getParamId());
                m.setText(p.getParamName());
                m.setPid(p.getParamType());
                return m;
            }
        });
        Assert.assertNotNull(json);
    }

    private List<Param> getList() {
        List<Param> list = new ArrayList<Param>();
        Param p1 = new Param();
        p1.setParamId(1);
        p1.setParamName("param1");
        p1.setParamDesc("desc1");
        p1.setParamKey("{{key1}");
        p1.setParamType(0);
        list.add(p1);
        Param p2 = new Param();
        p2.setParamId(2);
        p2.setParamName("param2");
        p2.setParamDesc("desc2");
        p2.setParamKey("{{key2}");
        p2.setParamType(1);
        list.add(p2);
        Param p3 = new Param();
        p3.setParamId(3);
        p3.setParamName("param3");
        p3.setParamDesc("desc3");
        p3.setParamKey("{{key3}");
        p3.setParamType(5);
        list.add(p3);
        Param p4 = new Param();
        p4.setParamId(4);
        p4.setParamName("param4");
        p4.setParamDesc("desc4");
        p4.setParamKey("{{key4}");
        p4.setParamType(5);
        list.add(p4);
        Param p5 = new Param();
        p5.setParamId(5);
        p5.setParamName("param5");
        p5.setParamDesc("desc5");
        p5.setParamKey("{{key5}");
        p5.setParamType(0);
        list.add(p5);
        Param p6 = new Param();
        p6.setParamId(6);
        p6.setParamName("param6");
        p6.setParamDesc("desc6");
        p6.setParamKey("{{key6}");
        p6.setParamType(1);
        list.add(p6);
        return list;
    }
}