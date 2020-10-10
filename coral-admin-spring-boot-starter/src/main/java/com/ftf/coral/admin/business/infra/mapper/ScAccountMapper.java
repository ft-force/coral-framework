package com.ftf.coral.admin.business.infra.mapper;

import java.util.List;
import java.util.Map;

import javax.swing.Spring;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.ftf.coral.admin.core.entity.ScAccount;
import com.ftf.coral.core.page.PageRequest;
import com.ftf.coral.util.StringUtils;

@Mapper
public interface ScAccountMapper {

    @Insert({"INSERT INTO sc_account(username,category,status,creation_user,last_modified_user)",
        "VALUES(#{username},#{category},#{status},#{creationUser},#{lastModifiedUser})"})
    @SelectKey(statement = "SELECT last_insert_id()", keyProperty = "id", before = false, resultType = long.class)
    long save(ScAccount scAccount);

    @Update({"<script>", "UPDATE sc_account", "<set>", "<if test='username != null'>username=#{username},</if>",
        "<if test='category != null'>category=#{category},</if>", "<if test='status != null'>status=#{status},</if>",
        "<if test='lastModifiedUser != null'>last_modified_user=#{lastModifiedUser},</if>",
        "<if test='deleted != null'>deleted=#{deleted},</if>", "</set>", "WHERE id=#{id}", "</script>"})
    int update(ScAccount scAccount);

    @Select("SELECT count(*) FROM sc_account WHERE username=#{username}")
    boolean existByUsername(String username);

    @Delete("DELETE FROM sc_account WHERE id=#{id}")
    int deleteById(Long id);

    @Select("SELECT id, username, category, status FROM sc_account WHERE deleted=0 and username=#{username}")
    @Results(id = "scAccountResult",
        value = {@Result(property = "creationDate", column = "creation_date"),
            @Result(property = "creationUser", column = "creation_user"),
            @Result(property = "lastModifiedDate", column = "last_modified_date"),
            @Result(property = "lastModifiedUser", column = "last_modified_user")})
    ScAccount selectScAccount(String username);

    @SelectProvider(type = Provider.class, method = "pageSelect")
    List<Map<Spring, Object>> pageSelect(@Param("page") PageRequest page);

    class Provider {
        /* 批量插入 */
        public String pageSelect(Map map) {
            PageRequest page = (PageRequest)map.get("page");
            StringBuilder sb = new StringBuilder();
            sb.append(
                "SELECT sa.id, sa.username, sa.category, sa.status, sa.creation_date creationDate, sa.creation_user creationUser, sa.last_modified_date lastModifiedDate, sa.last_modified_user lastModifiedUser, GROUP_CONCAT(sar.role_code) roleGroup FROM sc_account sa left join sc_account_role sar on sa.id=sar.account_id and sar.deleted=0 WHERE sa.deleted=0");

            if (StringUtils.isNotBlank((String)page.getConditionMap().get("username"))) {
                sb.append(" and sa.username=#{page.conditionMap.username,jdbcType=VARCHAR}");
            }
            if (StringUtils.isNotBlank((String)page.getConditionMap().get("status"))) {
                sb.append(" and sa.status=#{page.conditionMap.status,jdbcType=VARCHAR}");
            }
            sb.append(" GROUP BY sa.id ORDER BY sa.creation_date DESC");
            return sb.toString();
        }
    }
}