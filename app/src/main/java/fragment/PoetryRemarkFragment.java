package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.mypoetry.R;

import model.Poetry;
import zuo.biao.library.base.BaseModel;


public class PoetryRemarkFragment
        extends Fragment
        implements PoetryFragmentInterface{

    private View contentView;
    private TextView tvRemark;
    private TextView tvTranslation;

    private Poetry poetry;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        contentView = inflater.inflate(R.layout.fragment_poetry_remark, null);
        initView();
        return contentView;
    }

    private void initView() {
        tvRemark = contentView.findViewById(R.id.tv_poetry_remark);
        tvTranslation = contentView.findViewById(R.id.tv_poetry_translation);

        if (poetry != null) {
            tvRemark.setText(poetry.getRemark());
            tvTranslation.setText(poetry.getTranslation());
        }
    }

    @Override
    public void bindData(BaseModel baseModel) {
        poetry = (Poetry) baseModel;
    }

}