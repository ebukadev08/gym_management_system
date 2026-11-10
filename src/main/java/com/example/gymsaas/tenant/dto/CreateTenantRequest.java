package com.example.gymsaas.tenant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTenantRequest(

        @NotBlank(message = "Gym name is required")
        @Size(max = 150, message = "Gym name is too long")
        String gymName,

        @NotBlank(message = "Slug is required")
        @Pattern(regexp = "^[a-z0-9]+(-[a-z0-9]+)*$",
                message = "Slug can only have lowercase letters, numbers and hyphens")
        @Size(max = 100, message = "Slug is too long")
        String slug,

        @NotBlank(message = "Currency code is required")
        @Size(min = 3, max = 3, message = "Currency code must be 3 letters")
        String defaultCurrencyCode,

        @NotBlank(message = "Language code is required")
        @Size(max = 5, message = "Language code is too long")
        String defaultLanguageCode,

        @NotBlank(message = "Admin email is required")
        @Email(message = "Admin email is not valid")
        String adminEmail,

        @NotBlank(message = "Admin first name is required")
        String adminFirstName,

        @NotBlank(message = "Admin last name is required")
        String adminLastName
) {
}