package com.fengluo.dto.request;

import lombok.*;

import java.io.Serializable;

/**
 * @Author: fengluo
 * @Date: 2024/4/10 22:27
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SampleRequest implements Serializable {

    private Long businessId;

}
