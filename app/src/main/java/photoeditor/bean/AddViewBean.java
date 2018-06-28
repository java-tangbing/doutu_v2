package photoeditor.bean;

import android.view.View;

import photoeditor.ViewType;

public class AddViewBean {
    private View view;
    private View addView;
    private ViewType type;
    private String childImagePath;

    public AddViewBean(View view, View addView, String path, ViewType type) {
        this.view = view;
        this.type = type;
        this.addView = addView;
        this.childImagePath = path;
    }

    public String getChildImagePath() {
        return childImagePath;
    }

    public void setChildImagePath(String childImagePath) {
        this.childImagePath = childImagePath;
    }

    public View getAddView() {
        return addView;
    }

    public void setAddView(View addView) {
        this.addView = addView;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ViewType getType() {
        return type;
    }

    public void setType(ViewType type) {
        this.type = type;
    }
}
