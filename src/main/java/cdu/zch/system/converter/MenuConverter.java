package cdu.zch.system.converter;

import cdu.zch.system.model.entity.SysMenu;
import cdu.zch.system.model.form.MenuForm;
import cdu.zch.system.model.vo.MenuVO;
import org.mapstruct.Mapper;

/**
 * 菜单对象转换器
 * @author Zch
 * @date 2023/7/11
 **/
@Mapper(componentModel = "spring")
public interface MenuConverter {

    MenuVO entity2Vo(SysMenu entity);


    MenuForm entity2Form(SysMenu entity);

    SysMenu form2Entity(MenuForm menuForm);

}
