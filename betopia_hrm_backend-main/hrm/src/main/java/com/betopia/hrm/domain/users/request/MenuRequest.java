package com.betopia.hrm.domain.users.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuRequest(
        Long id,

        Long permission_id,

        Long parentId,

        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotNull(message = "URL cannot be null")
        @NotBlank(message = "URL cannot be blank")
        String url,

        @NotNull(message = "Icon cannot be null")
        @NotBlank(message = "Icon cannot be blank")
        String icon,

        boolean headerMenu,

        boolean sidebarMenu,

        boolean dropdownMenu,

        boolean childrenParentMenu,

        boolean status,

        Long menuOrder
) {
}
