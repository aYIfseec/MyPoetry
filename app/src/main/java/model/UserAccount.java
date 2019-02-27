package model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zuo.biao.library.base.BaseModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserAccount extends BaseModel {

	private Long uid;
	private String phone;
	private String nickName;
	private String pwd;

	@Override
	public boolean checkCorrect() {
		return uid != null && nickName != null;
	}
}
