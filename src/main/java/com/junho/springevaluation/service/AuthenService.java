package com.junho.springevaluation.service;

import com.junho.springevaluation.data.dto.AuthenDTO;
import com.junho.springevaluation.data.entity.AuthenEntity;
import com.junho.springevaluation.data.entity.RoleEntity;
import com.junho.springevaluation.data.repository.AuthenEntityRepository;
import com.junho.springevaluation.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenService implements UserDetailsService {
    private final AuthenEntityRepository authenEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 정보를 데이터베이스에서 가져오고
        AuthenEntity authenEntity = authenEntityRepository.findByUsername(username);

        // 사용자가 존재하지 않을 경우 예외를 던지고
        if (authenEntity == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // 권한을 가져옴
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(authenEntity.getRole().getName()));

        // UserDetails 객체를 생성하여 반환
        return new User(
                authenEntity.getUsername(),
                authenEntity.getPassword(),
                grantedAuthorities
        );
    }

    @Transactional // 트랜잭션 관리
    public boolean join(AuthenDTO authenDTO) {
        if (!this.authenEntityRepository.existsByUsername(authenDTO.getUsername())) {
            String password = passwordEncoder.encode(authenDTO.getPassword());

            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName("ROLE_USER"); // 역할 설정

            AuthenEntity authenEntity = AuthenEntity.builder()
                    .username(authenDTO.getUsername())
                    .password(password)
                    .role(roleEntity)
                    .build();

            this.authenEntityRepository.save(authenEntity);
            return true;
        }
        throw new UserAlreadyExistsException("User already exists with username: " + authenDTO.getUsername());
    }
}