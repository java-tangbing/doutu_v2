package com.pufei.gxdt.module.maker.view;

import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;

public interface EditImageView extends BaseView<EditImagePresenter> {
    void upLoadImageResult(ModifyResultBean response);
    void downloadImageResult(String base64,int type);
}
