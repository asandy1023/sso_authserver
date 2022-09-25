package org.example.authserver.service;

import org.example.authserver.client.ResourceServerClient;
import org.example.authserver.client.bo.CheckPassWordBO;
import org.example.authserver.client.dto.CheckPassWordDTO;
import org.example.authserver.entity.ResponseResult;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author JQ
 */
@Service
public class BaseUserDetailService implements UserDetailsService {

    /**
     * 遠程Feign調用接口依賴
     */
    @Autowired
    ResourceServerClient resourceServerClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CheckPassWordDTO checkPassWordDTO = CheckPassWordDTO.builder().userName(username).build();
        ResponseResult<CheckPassWordBO> responseResult = resourceServerClient.checkPassWord(checkPassWordDTO);
        CheckPassWordBO checkPassWordBO = responseResult.getData();
        List<GrantedAuthority> authorities = new ArrayList<>();
        // return用戶權限信息
        User user = new User(checkPassWordBO.getUserName(),
                checkPassWordBO.getPassWord() + "," + checkPassWordBO.getSalt(), true, true, true, true, authorities);
        return user;
    }
}
