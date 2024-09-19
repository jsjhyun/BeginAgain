package com.team3.user.dto;

import com.team3.user.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageDto {

    private Long id;
    private String email;
    private String username;
    private String nickname;
}
