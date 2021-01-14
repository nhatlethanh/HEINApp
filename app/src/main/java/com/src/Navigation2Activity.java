package com.src;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.src.Model.OrderProvisional;
import com.src.Module.Category.view.CateFragment;
import com.src.Module.Favourite.view.FavouriteFragment;
import com.src.Module.Home.view.ExploreFragment;
import com.src.Module.Home.view.ExploreFragment2;
import com.src.Module.Order.view.MyOrderActivity;
import com.src.Module.User.view.ProfileFragment;
import com.src.Utils.ViewPagerNavigationAdapter;

import java.util.ArrayList;


public class Navigation2Activity extends AppCompatActivity {

    public static ArrayList<OrderProvisional> orderDetails;
    private ViewPager viewPager;
    public static BottomNavigationView navigation;
    private MenuItem prevMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_navigation2);
        navigation = findViewById(R.id.navigation2);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = findViewById(R.id.viewNavigation2);
        viewPager.setOffscreenPageLimit(1);

        //order Details
        if (orderDetails == null) {
            orderDetails = new ArrayList<>();
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("FsBs", "onPageSelected: " + position);
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }

                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        setupViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerNavigationAdapter adapter = new ViewPagerNavigationAdapter(getSupportFragmentManager());
        ExploreFragment2 exploreFragment = new ExploreFragment2();
        CateFragment cateFragment = new CateFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        adapter.addFragment(exploreFragment);
        adapter.addFragment(cateFragment);

        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        switch (item.getItemId()) {
            case R.id.navigation_explore:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_cate:
                viewPager.setCurrentItem(1);

                return true;
            case R.id.navigation_profile:
                viewPager.setCurrentItem(3);

                return true;
        }
        return false;
    };




}
