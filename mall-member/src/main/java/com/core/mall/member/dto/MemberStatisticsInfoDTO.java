package com.core.mall.member.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 会员统计信息
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class MemberStatisticsInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long memberId;

	private BigDecimal consumeAmount;

	private BigDecimal couponAmount;

	private Integer orderCount;

	private Integer couponCount;

	private Integer commentCount;

	private Integer returnOrderCount;

	private Integer loginCount;

	private Integer attendCount;

	private Integer fansCount;

	private Integer collectProductCount;

	private Integer collectSubjectCount;

	private Integer collectCommentCount;

	private Integer inviteFriendCount;


}
