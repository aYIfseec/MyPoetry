package model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zuo.biao.library.base.BaseModel;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Comment extends BaseModel {

    private String commentId;

    /**
     * root comment id for poetry
     * if current is root , generator id
     */
    private Long rootId;

    /**
     * poetryId
     * or
     * parent comment id
     */
    private Long parentId;

    private Long uid;

    private String nickName;

    private String poetryTitle;

    private String content;

    private Integer resourceType;

    private String resourceUrl;

    private Integer replyCount;

    private Integer likeCount;

    private Integer readCount;

    private Long heatCount;

    private Date createTime;

    private Boolean likeStatus;

    private Boolean readStatus;

    @Override
    public boolean checkCorrect() {
        return true;
    }
}
