package com.core.mall.member.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 成长值变化历史记录
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class GrowthChangeHistoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long memberId;

	private Date createTime;

	private Integer changeCount;

	private String note;

	private Integer sourceType;


}
