package seniordesign.phoneafriend;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import seniordesign.phoneafriend.main_screen_adapters.MainScreenPagerAdapter;
import seniordesign.phoneafriend.main_screen_fragments.contacts;
import seniordesign.phoneafriend.main_screen_fragments.inbox;
import seniordesign.phoneafriend.main_screen_fragments.main_selections;
import seniordesign.phoneafriend.main_screen_fragments.settings;

public class main_screen extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        List<Fragment> listFragments = new ArrayList<>();
        listFragments.add(new main_selections());
        listFragments.add(new contacts());
        listFragments.add(new inbox());
        listFragments.add(new settings());

        MainScreenPagerAdapter mainScreenPagerAdapter = new MainScreenPagerAdapter(getSupportFragmentManager(),listFragments);

        viewPager.setAdapter(mainScreenPagerAdapter);

    }
}
