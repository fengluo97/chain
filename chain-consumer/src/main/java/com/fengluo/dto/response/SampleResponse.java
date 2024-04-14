package com.fengluo.dto.response;

import lombok.*;

import java.util.Map;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:28
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SampleResponse {

    private Boolean success;

    private Map<String, Object> params;

}
