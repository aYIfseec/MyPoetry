package model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poetry {

    private Long poetryId;
    private String title;
    private String author;
    private String dynasty;
    private String content;
    private String remark;
    private String translation;
    private String comment;

}
