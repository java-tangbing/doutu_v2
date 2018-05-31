package com.pufei.gxdt.module.maker.view;

import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;

public interface EditImageView extends BaseView<EditImagePresenter> {
    void upLoadImageResult(String response);
    void downloadImageResult(String  base64);
}
