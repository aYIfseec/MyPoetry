package model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zuo.biao.library.base.BaseModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Poetry extends BaseModel {

    private Long poetryId;
    private String title;
    private String author;
    private String dynasty;
    private String content;
    private String remark;
    private String translation;
    private String comment;

    @Override
    protected boolean checkCorrect() {
        return poetryId != null;
    }
}
