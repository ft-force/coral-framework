package com.ftf.coral.admin.business.infra.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;

import com.ftf.coral.admin.core.entity.ScApplicationAdmin;
import com.ftf.coral.core.page.PageRequest;
import com.ftf.coral.util.StringUtils;

@Mapper
public interface ScApplicationAdminMapper {

    @Insert({"INSERT INTO sc_application_admin(application_id,account_id,creation_user,last_modified_user)",
        "VALUES(#{applicationId},#{accountId},#{creationUser},#{lastModifiedUser})"})
    @SelectKey(statement = "SELECT last_insert_id()", keyProperty = "id", before = false, resultType = long.class)
    long save(ScApplicationAdmin scApplicationAdmin);

    @Select("SELECT count(*) FROM sc_application_admin WHERE application_id=#{applicationId} and account_id=#{accountId}")
    boolean exist(String applicationId, Long accountId);

    @Select("SELECT application_id FROM sc_application_admin WHERE account_id=#{accountId}")
    List<String> selectByAccountId(Long accountId);

    @Delete("DELETE FROM sc_application_admin WHERE id=#{id}")
    int deleteById(Long id);

    @Select("SELECT application_id FROM sc_application_admin WHERE id=#{id}")
    String selectApplicationIdById(Long id);

    @Delete("DELETE FROM sc_application_admin WHERE application_id=#{applicationId}")
    int deleteByApplicationId(String applicationId);

    @SelectProvider(type = Provider.class, method = "pageSelect")
    List<Map<String, Object>> pageSelect(@Param("page") PageRequest page);

    class Provider {
        /* 批量插入 */
        public String pageSelect(Map map) {
            PageRequest page = (PageRequest)map.get("page");
            StringBuilder sb = new StringBuilder();
            sb.append(
                "SELECT saa.id, saa.account_id accountId, saa.application_id applicationId, sa.username, sa.category, sa.status, saa.creation_date creationDate, saa.creation_user creationUser, saa.last_modified_date lastModifiedDate, saa.last_modified_user lastModifiedUser FROM sc_application_admin saa left join sc_account sa on saa.account_id=sa.id and sa.deleted=0 WHERE saa.deleted=0");

            if (StringUtils.isNotBlank((String)page.getConditionMap().get("applicationId"))) {
                sb.append(" and saa.application_id=#{page.conditionMap.applicationId,jdbcType=VARCHAR}");
            }
            if (StringUtils.isNotBlank((String)page.getConditionMap().get("username"))) {
                sb.append(" and sa.username=#{page.conditionMap.username,jdbcType=VARCHAR}");
            }
            sb.append(" ORDER BY saa.creation_date DESC");
            return sb.toString();
        }
    }
}