package com.team3.user.mapper;

import com.team3.user.dto.MyPageDto;
import com.team3.user.entity.User;

public class UserMapper {

    // 엔티티 -> DTO
    public static MyPageDto toDTO(User user) {
        return MyPageDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }

    // DTO -> 엔티티
    public static User toEntity(MyPageDto userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .nickname(userDTO.getNickname())
                .build();
    }
}