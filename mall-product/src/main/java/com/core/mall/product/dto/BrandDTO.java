package com.core.mall.product.dto;

import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;
import java.util.Date;


/**
 * 品牌
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Data
public class BrandDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@NotNull(message = "更新数据品牌id不能为空", groups = {UpdateGroup.class})
	@Null(message = "添加数据品牌id必须为空", groups = {AddGroup.class})
	@JsonSerialize(using = ToStringSerializer.class)
	private Long brandId;

	/**
	 * 新增的时候不能为空，更新的时候可以为空
	 */
	@NotBlank(message = "品牌名称不能为空", groups = {AddGroup.class})
	private String name;

	@NotBlank(message = "logo 不能为空" , groups = {AddGroup.class})
	@URL(message = "logo 地址不合法")
	private String logo;

	private String descript;

	private Integer showStatus;

	private String firstLetter;

	private Integer sort;


}
