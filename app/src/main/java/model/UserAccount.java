package model;


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
	private String token;
	private String pwd;

	@Override
	public boolean checkCorrect() {
		return uid != null;
	}
}
