package ja.burhanrashid52.photoeditor;

import android.view.View;

public class AddViewBean {
    private View view;
    private ViewType type;

    public AddViewBean(View view, ViewType type) {
        this.view = view;
        this.type = type;
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
