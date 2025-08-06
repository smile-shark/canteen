package com.smileshark;

import cn.hutool.core.util.IdUtil;
import com.smileshark.entity.Area;
import com.smileshark.entity.Province;
import com.smileshark.service.AreaService;
import com.smileshark.service.ProvinceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AreaInsert {
    @Autowired
    private AreaService areaService;
    @Autowired
    private ProvinceService provinceService;
    @Test
    public void AreaInsert() {
        // 创建省份列表并填充数据
        List<Province> provinceList = new ArrayList<>();

        // 北京市
        Province beijing = new Province().setName("北京市");
        List<Area> beijingAreas = new ArrayList<>();
        beijingAreas.add(new Area().setName("东城区"));
        beijingAreas.add(new Area().setName("西城区"));
        beijingAreas.add(new Area().setName("朝阳区"));
        beijingAreas.add(new Area().setName("海淀区"));
        beijingAreas.add(new Area().setName("丰台区"));
        beijing.setAreaList(beijingAreas);
        provinceList.add(beijing);

        // 上海市
        Province shanghai = new Province().setName("上海市");
        List<Area> shanghaiAreas = new ArrayList<>();
        shanghaiAreas.add(new Area().setName("黄浦区"));
        shanghaiAreas.add(new Area().setName("徐汇区"));
        shanghaiAreas.add(new Area().setName("长宁区"));
        shanghaiAreas.add(new Area().setName("静安区"));
        shanghaiAreas.add(new Area().setName("普陀区"));
        shanghai.setAreaList(shanghaiAreas);
        provinceList.add(shanghai);

        // 天津市
        Province tianjin = new Province().setName("天津市");
        List<Area> tianjinAreas = new ArrayList<>();
        tianjinAreas.add(new Area().setName("和平区"));
        tianjinAreas.add(new Area().setName("河东区"));
        tianjinAreas.add(new Area().setName("河西区"));
        tianjinAreas.add(new Area().setName("南开区"));
        tianjinAreas.add(new Area().setName("河北区"));
        tianjin.setAreaList(tianjinAreas);
        provinceList.add(tianjin);

        // 重庆市
        Province chongqing = new Province().setName("重庆市");
        List<Area> chongqingAreas = new ArrayList<>();
        chongqingAreas.add(new Area().setName("渝中区"));
        chongqingAreas.add(new Area().setName("江北区"));
        chongqingAreas.add(new Area().setName("沙坪坝区"));
        chongqingAreas.add(new Area().setName("九龙坡区"));
        chongqingAreas.add(new Area().setName("南岸区"));
        chongqing.setAreaList(chongqingAreas);
        provinceList.add(chongqing);

        // 河北省
        Province hebei = new Province().setName("河北省");
        List<Area> hebeiAreas = new ArrayList<>();
        hebeiAreas.add(new Area().setName("石家庄市"));
        hebeiAreas.add(new Area().setName("唐山市"));
        hebeiAreas.add(new Area().setName("秦皇岛市"));
        hebeiAreas.add(new Area().setName("邯郸市"));
        hebeiAreas.add(new Area().setName("保定市"));
        hebei.setAreaList(hebeiAreas);
        provinceList.add(hebei);

        // 广东省
        Province guangdong = new Province().setName("广东省");
        List<Area> guangdongAreas = new ArrayList<>();
        guangdongAreas.add(new Area().setName("广州市"));
        guangdongAreas.add(new Area().setName("深圳市"));
        guangdongAreas.add(new Area().setName("珠海市"));
        guangdongAreas.add(new Area().setName("佛山市"));
        guangdongAreas.add(new Area().setName("东莞市"));
        guangdong.setAreaList(guangdongAreas);
        provinceList.add(guangdong);

        // 江苏省
        Province jiangsu = new Province().setName("江苏省");
        List<Area> jiangsuAreas = new ArrayList<>();
        jiangsuAreas.add(new Area().setName("南京市"));
        jiangsuAreas.add(new Area().setName("苏州市"));
        jiangsuAreas.add(new Area().setName("无锡市"));
        jiangsuAreas.add(new Area().setName("常州市"));
        jiangsuAreas.add(new Area().setName("徐州市"));
        jiangsu.setAreaList(jiangsuAreas);
        provinceList.add(jiangsu);

        // 其他省份可以按照同样的格式继续添加...

        // 循环保存所有省份和对应的区域
        for (Province province : provinceList) {
            // 生成32位UUID作为省份ID
            String provinceId = IdUtil.simpleUUID();
            province.setProvinceId(provinceId);
            provinceService.save(province);

            // 保存该省份下的所有区域
            if (province.getAreaList() != null && !province.getAreaList().isEmpty()) {
                for (Area area : province.getAreaList()) {
                    // 生成32位UUID作为区域ID
                    String areaId = IdUtil.simpleUUID();
                    area.setAreaId(areaId);
                    area.setProvinceId(provinceId);
                    areaService.save(area);
                }
            }
        }
    }

}
