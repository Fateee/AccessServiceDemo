package showapplist;

import android.support.v4.app.Fragment;

public class AppsActivity extends SingleFragmentActivity {

	@Override
    protected Fragment createFragment() {
        return new AppListFragment();
    }

}
