package com.br.gomesdee87.controlesemanal.admin.dto;

import com.br.gomesdee87.controlesemanal.user.User;

public record AdminUserResponse(Long id, String name, String telephone,
                                boolean passwordChangeRequired, boolean hasPassword) {
    public static AdminUserResponse from(User user) {
        return new AdminUserResponse(user.getId(), user.getName(), user.getTelephone(),
                Boolean.TRUE.equals(user.getPasswordChangeRequired()), user.getPasswordHash() != null);
    }
}