package com.Library.services.dtos;

import lombok.Builder;

@Builder
public record ChangeUserRoleDTO(String userEmail, String userRole) {
}
