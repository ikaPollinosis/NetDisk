package com.netdisk.service.impl;

import com.netdisk.entity.User;
import com.netdisk.entity.UserDisplay;
import com.netdisk.mapper.UserMapper;
import com.netdisk.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: UserServiceImpl
 * @Description: 用户业务实现类
 * @Date: 2022/4/29 15:55
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {
    /**
     * @description 添加新的用户
     * @param user 对象
     * @return 影响行数
     */
    @Override
    public boolean addUser(User user){
        if(userMapper.addUser(user)==1) return true;
        return false;
    }

    /**
     * @description 按ID删除用户
     * @param userId
     * @return 影响行数
     */
    @Override
    public boolean deleteById(Integer userId){
        if(userMapper.deleteById(userId)==1) return true;
        return false;
    }

    /**
     * @description 按ID查找
     * @param userId
     * @return User对象
     */
    @Override
    public User queryById(Integer userId){
        return userMapper.queryById(userId);
    }

    /**
     * @Description  按openID查找
     * @Param openId
     * @return User对象
     **/
    @Override
    public User queryByOpenId(String openId){
        return userMapper.queryByOpenId(openId);
    }

    /**
     * @description 按邮箱查找
     * @param email
     * @return User对象
     */
    @Override
    public User queryByEmail(String email){
        return userMapper.queryByEmail(email);
    }

    /**
     * @description 按邮箱和密码查找
     * @param password
     * @param email
     * @return User对象
     */
    @Override
    public User queryByEmailPwd(String email, String password){
        return userMapper.queryByEmailPwd(email,password);
    }

    /**
     * @description 获取全部数据
     * @return User对象列表
     */
    @Override
    public List<User> queryAll(){
        return userMapper.queryAll();
    }

    /**
     * @description 使用User筛选用户
     * @param user
     * @return User对象列表
     */
    @Override
    public List<User> queryAll(User user){
        return userMapper.queryAll(user);
    }

    /**
     * @description 修改用户
     * @param user
     * @return 影响行数
     */
    @Override
    public boolean update(User user){
        if(userMapper.update(user) == 1)return true;
        return false;
    }

    /**
     * @description 获取所有需要显示的对象
     * @return User对象列表
     */
    @Override
    public List<UserDisplay> queryUserDisplay(){
        return userMapper.queryUserDisplay();
    }

    /**
     * @description 获取注册用户数量
     * @return 用户数量
     */
    @Override
    public Integer getUserCount(){
        return userMapper.getUserCount();
    }
}
