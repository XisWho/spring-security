package com.xw.customize.userDetailService;

import com.xw.dao.UserDAO;
import com.xw.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class MyUserDetailService implements UserDetailsService {

    private  final UserDAO userDao;

    @Autowired
    public MyUserDetailService(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loadUserByUsername(username);
        if(ObjectUtils.isEmpty(user)) {
            throw new RuntimeException("用户不存在");
        }
        user.setRoles(userDao.getRolesByUid(user.getId()));
        return user;
    }

}
