package com.example.yunjeong.project1;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.yunjeong.project1.dummy.DummyContent;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.BottomBarFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BottombarActivity extends AppCompatActivity  implements StoreMenuFragment.OnListFragmentInteractionListener{

    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/koverwatch.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_bottombar);

        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userid = preferences.getString(Config.USERID_SHARED_PREF, "no");
        Log.d("pref id:", userid);
        bottomBar = BottomBar.attach(this, savedInstanceState);


        bottomBar.setFragmentItems(getSupportFragmentManager(), R.id.fragmentContainer,
                    new BottomBarFragment(HomeFragment.newInstance(userid), R.drawable.inlove24, "Home"),
                    new BottomBarFragment(TicketFragment.newInstance(userid), R.drawable.ticket25, "Ticket"),
                    new BottomBarFragment(new StoreMenuFragment(), R.drawable.shop25, "Store" ),
                    new BottomBarFragment(new UserFragment(), R.drawable.user24, "User")
        );

        bottomBar.mapColorForTab(0, "#3B494C");
        bottomBar.mapColorForTab(1, "#00796B");
        bottomBar.mapColorForTab(2, "#7B1FA2");
        bottomBar.mapColorForTab(3, "#FF5252");
        Log.i("where tab:", "beacon");

        String menuFragment = "";
        menuFragment = this.getIntent().getStringExtra("storeid");
        if(menuFragment != null) {
            if(menuFragment.equals("S0005")) {
                Log.i("intent start:", "fragment" + menuFragment);
                bottomBar.setDefaultTabPosition(2);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, StoreMenuFragment.newInstance(menuFragment)).commit();

            }
        }
        // Make a Badge for the first tab, with red background color and a value of "4".
        // TODO: 나중에 이용하거나 DELETE
//        BottomBarBadge unreadMessages = bottomBar.makeBadgeForTabAt(1, "#E91E63", 4);

        // Control the badge's visibility
        //unreadMessages.hide();

        // Change the displayed count for this badge.
        //unreadMessages.setCount(4);

        // Change the show / hide animation duration.

        // If you want the badge be shown always after unselecting the tab that contains it.
        //unreadMessages.setAutoShowAfterUnSelection(true);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
