package com.zeroone.star.project.vo.j3.capital;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ApiModel("核销单修改VO")
public class VerificationSheetModifyVO extends VerificationSheetAddVO{
}
