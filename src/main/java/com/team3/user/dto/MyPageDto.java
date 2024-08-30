package com.team3.user.dto;

import com.team3.user.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageDto {

    private Integer id;
    private String email;
    private String username;
    private String nickname;

    // entity -> dto
    public MyPageDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
