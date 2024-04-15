package com.fengluo.dto.response;

import lombok.*;

import java.io.Serializable;
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
public class SampleResponse implements Serializable {

    private Boolean success;

    private Map<String, Object> params;

}
