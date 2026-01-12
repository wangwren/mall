package com.core.mall.member.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 会员
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class MemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long levelId;

	private String username;

	private String password;

	private String nickname;

	private String mobile;

	private String email;

	private String header;

	private Integer gender;

	private Date birth;

	private String city;

	private String job;

	private String sign;

	private Integer sourceType;

	private Integer integration;

	private Integer growth;

	private Integer status;

	private Date createTime;


}
