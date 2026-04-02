package com.zeroone.star.sysconfig.service.impl;

import com.zeroone.star.project.dto.j1.sysconfig.MenuAddDTO;
import com.zeroone.star.project.dto.j1.sysconfig.MenuUpdateDTO;
import com.zeroone.star.project.vo.j1.sysconfig.MenuDetailVO;
import com.zeroone.star.sysconfig.entity.MenuDO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MsMenuMapper {


    /**
     * DTO转DO，用来将前端添加或者修改表中的数据，转化为符合数据库的字段并添加
     * @param src
     * @return
     */
    MenuDO DTO_DO(MenuUpdateDTO src);

    MenuDO AddDtoToMenu(MenuAddDTO dto);
    MenuDetailVO MenuToDetailVO(MenuDO menuDO);

}
