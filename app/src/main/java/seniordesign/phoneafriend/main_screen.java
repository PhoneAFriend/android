package seniordesign.phoneafriend;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

import seniordesign.phoneafriend.main_screen_adapters.MainScreenPagerAdapter;
import seniordesign.phoneafriend.main_screen_fragments.contacts_section;
import seniordesign.phoneafriend.main_screen_fragments.inbox;
import seniordesign.phoneafriend.main_screen_fragments.main_selections;
import seniordesign.phoneafriend.main_screen_fragments.settings;

public class main_screen extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    ViewPager viewPager;
    TabHost tabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        initViewPager();
        initTabHost();


    }

    private void initTabHost() {

        tabHost = (TabHost) findViewById(R.id.main_tabHost);
        tabHost.setup();

        String[] tabNames = {"Main", "Contacts", "Inbox", "Settings"};
        Resources res = getResources();
        for (int i = 0; i < tabNames.length; i++) {
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i], ResourcesCompat.getDrawable(getResources(), android.R.drawable.ic_menu_save, null));
            tabSpec.setContent(new FakeContent(getApplicationContext()));
            tabHost.addTab(tabSpec);
        }

        tabHost.setOnTabChangedListener(this);

    }

    public class FakeContent implements TabHost.TabContentFactory {

        Context context;

        public FakeContent(Context mycontext) {
            context = mycontext;
        }

        @Override
        public View createTabContent(String tag) {

            View fakeView = new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);

            return fakeView;
        }
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        List<Fragment> listFragments = new ArrayList<>();
        listFragments.add(new main_selections());
        listFragments.add(new contacts_section());
        listFragments.add(new inbox());
        listFragments.add(new settings());

        MainScreenPagerAdapter mainScreenPagerAdapter = new MainScreenPagerAdapter(
                getSupportFragmentManager(), listFragments);
        viewPager.setAdapter(mainScreenPagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }


    @Override
    public void onTabChanged(String tabId) {
        int selectedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedItem);

        HorizontalScrollView hScroll = (HorizontalScrollView) findViewById(R.id.tab_horz_scroller);
        View tabView = tabHost.getCurrentTabView();
        int scrollPos = tabView.getLeft() - (hScroll.getWidth() - tabView.getWidth()) / 2;
        hScroll.smoothScrollTo(scrollPos, 0);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int selectedItem) {
        tabHost.setCurrentTab(selectedItem);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}