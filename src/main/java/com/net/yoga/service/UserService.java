package com.net.yoga.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.net.yoga.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaort
 * @since 2020-08-02
 */
public interface UserService extends IService<User> {

    public List<User> findAllUser();
}
