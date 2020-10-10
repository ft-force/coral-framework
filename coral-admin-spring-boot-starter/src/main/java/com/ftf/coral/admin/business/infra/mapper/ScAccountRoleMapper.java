package com.ftf.coral.admin.business.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.ftf.coral.admin.core.entity.ScAccountRole;

@Mapper
public interface ScAccountRoleMapper {

    @Insert({"INSERT INTO sc_account_role(account_id,role_code,creation_user,last_modified_user)",
        "VALUES(#{accountId},#{roleCode},#{creationUser},#{lastModifiedUser})"})
    @SelectKey(statement = "SELECT last_insert_id()", keyProperty = "id", before = false, resultType = long.class)
    long save(ScAccountRole scAccountRole);

    @Delete("DELETE FROM sc_account_role WHERE account_id=#{accountId}")
    int deleteByAccountId(Long accountId);

    @Select("SELECT role_code FROM sc_account_role WHERE deleted=0 and account_id=#{accountId}")
    List<String> selectRoleCodeList(Long accountId);
}