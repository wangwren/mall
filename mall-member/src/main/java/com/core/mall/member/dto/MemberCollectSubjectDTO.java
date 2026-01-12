package com.core.mall.member.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 会员收藏的专题活动
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class MemberCollectSubjectDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long subjectId;

	private String subjectName;

	private String subjectImg;

	private String subjectUrll;


}
