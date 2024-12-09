package com.junho.springevaluation.service;

import com.junho.springevaluation.data.dto.AuthenDTO;
import com.junho.springevaluation.data.entity.AuthenEntity;
import com.junho.springevaluation.data.entity.RoleEntity;
import com.junho.springevaluation.data.repository.AuthenEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final AuthenEntityRepository authenEntityRepository;
    private final PasswordEncoder passwordEncoder;

    // RoleEntity 객체 생성
    RoleEntity roleEntity = new RoleEntity();

    public boolean join(AuthenDTO authenDTO) {
        if(!this.authenEntityRepository.existsByUsername(authenDTO.getUsername())) {
            String password = passwordEncoder.encode(authenDTO.getPassword());
            AuthenEntity authenEntity = AuthenEntity.builder()
                    .username(authenDTO.getUsername())
                    .password(password)
                    .role(roleEntity)
                    .build();
            this.authenEntityRepository.save(authenEntity);
            return true;
        }
        return false;
    }
}