package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Poetry;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnMsg {

    private int resCode;
    private String resMsg;
    private Poetry resData;
    private int totalCount;

}
