package com.example.generaltemplate.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.example.generaltemplate.entity.User;
import com.example.generaltemplate.mapper.UserMapper;
import com.example.generaltemplate.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现。
 *
 * @author <a href="连接">程序员</a>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{

}
