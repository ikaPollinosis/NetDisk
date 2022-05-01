package com.netdisk.mapper;

import com.netdisk.entity.User;
import com.netdisk.entity.UserDisplay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @InterfaceName: UserMapper
 * @Description: 数据库用户操作
 * @Date: 2022/4/28 19:05
 */
@Mapper
public interface UserMapper {
    /**
     * @description 添加新的用户
     * @param user 对象
     * @return 影响行数
     */
    int addUser(User user);

    /**
     * @description 按ID删除用户
     * @param userId
     * @return 影响行数
     */
    int deleteById(Integer userId);

    /**
     * @description 修改用户
     * @param user
     * @return 影响行数
     */
    int update(User user);

    /**
     * @description 按ID查找
     * @param userId
     * @return User对象
     */
    User queryById(Integer userId);

    /**
     * @description 按邮箱和密码查找
     * @param password
     * @param email
     * @return User对象
     */
    User queryByEmailPwd(String email, String password);

    /**
     * @Description  按openID查找
     * @Param openId
     * @return User对象
     **/
    User queryByOpenId(String openId);

    /**
     * @description 按邮箱查找
     * @param email
     * @return User对象
     */
    User queryByEmail(String email);

    /**
     * @description 获取全部数据
     * @return User对象列表
     */
    List<User> queryAll();

    /**
     * @description 使用User筛选用户
     * @param user
     * @return User对象列表
     */
    List<User> queryAll(User user);

    /**
     * @description 获取所有需要显示的对象
     * @return User对象列表
     */
    List<UserDisplay> queryUserDisplay();

    /**
     * @description 获取注册用户数量
     * @return 用户数量
     */
    Integer getUserCount();
}
