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
public class UserSession extends BaseModel {

    private User user;
    private String token;


    @Override
    public boolean checkCorrect() {
        return user != null && user.checkCorrect() && token != null;
    }
}
