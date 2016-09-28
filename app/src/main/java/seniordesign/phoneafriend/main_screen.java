package seniordesign.phoneafriend;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

import seniordesign.phoneafriend.main_screen_adapters.MainScreenPagerAdapter;
import seniordesign.phoneafriend.main_screen_fragments.contacts;
import seniordesign.phoneafriend.main_screen_fragments.inbox;
import seniordesign.phoneafriend.main_screen_fragments.main_selections;
import seniordesign.phoneafriend.main_screen_fragments.settings;

public class main_screen extends AppCompatActivity {

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

        String[] tabNames = {"Main","Contacts","Inbox","Settings"};

        for( int i = 0; i < tabNames.length; i++)
        {
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new FakeContent(getApplicationContext()));
            tabHost.addTab(tabSpec);
        }

    }

    public class FakeContent implements TabHost.TabContentFactory{

        Context context;
        public FakeContent(Context mycontext)
        {
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
        listFragments.add(new contacts());
        listFragments.add(new inbox());
        listFragments.add(new settings());

        MainScreenPagerAdapter mainScreenPagerAdapter = new MainScreenPagerAdapter(
                getSupportFragmentManager(),listFragments);
        viewPager.setAdapter(mainScreenPagerAdapter);
    }
}
