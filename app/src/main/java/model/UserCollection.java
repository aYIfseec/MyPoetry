package model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCollection {

    private Integer id;
    private Long poetryId;
    /**
     * 冗余的诗词名
     */
    private String title;
    private Long uid;
    private Date createTime;

}
