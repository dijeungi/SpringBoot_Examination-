package com.junho.springevaluation.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenDTO {

    // NotBlank, Size: 유혀성 검사 애너테이션 추가

    @NotBlank(message = "사용자 이름이 필요합니다.")
    @Size(min = 3, max = 20, message = "이름 길이는 3 ~ 20사이로 지정해주세요.")
    private String username;

    @NotBlank(message = "비밀번호가 필요합니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상으로 해주시기 바랍니다.")
    private String password;
}
