package com.restapi.ecommerce.security.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restapi.ecommerce.entity.User;
import com.restapi.ecommerce.repository.UserRepository;

/** ユーザ詳細のサービスクラス */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    /** ユーザ名によりユーザ詳細データを取得し、返却 */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザ名「" + username + "」のユーザを見つかりません。"));
        return UserDetailsImpl.build(user);
    }

}
