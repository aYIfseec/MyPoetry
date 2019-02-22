package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poetry implements Parcelable {
    private String id;
    private String notes;//诗词注释



    private Long poetryId;
    private String title;
    private String author;
    private String dynasty;
    private String content;
    private String remark;
    private String translation;
    private String comment;

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Poetry> CREATOR = new Creator<Poetry>() {

        @Override
        public Poetry createFromParcel(Parcel source) {
            // TODO Auto-generated method stub

            Poetry p = new Poetry();
            p.id = source.readString();
            p.title = source.readString();
            p.author = source.readString();
            p.notes = source.readString();
            p.content = source.readString();
            return p;

        }

        @Override
        public Poetry[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Poetry[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(notes);
        dest.writeString(content);
    }
}
