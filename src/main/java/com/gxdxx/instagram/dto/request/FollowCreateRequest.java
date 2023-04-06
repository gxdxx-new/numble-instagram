package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;

public record FollowCreateRequest(@JsonProperty("user_id") @Positive Long userId) {
}
