package model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

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
}
