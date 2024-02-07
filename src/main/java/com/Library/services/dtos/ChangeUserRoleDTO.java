package com.Library.services.dtos;

import lombok.Builder;

@Builder
public record ChangeUserRoleDTO(Long userId, String userRole) {
}
