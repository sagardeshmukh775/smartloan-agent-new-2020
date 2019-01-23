package com.smartloan.smtrick.smart_loan.view.activites;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.interfaces.OnFragmentInteractionListener;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.fragements.CalculatorFragment;
import com.smartloan.smtrick.smart_loan.view.fragements.GenerateLeedFragment;
import com.smartloan.smtrick.smart_loan.view.fragements.InvoicesTabFragment;
import com.smartloan.smtrick.smart_loan.view.fragements.LeedsFragment;
import com.smartloan.smtrick.smart_loan.view.fragements.ReportsFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.smartloan.smtrick.smart_loan.constants.Constant.REQUEST_CODE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.RESULT_CODE;

public class MainActivity extends AppCompatActivity
        implements OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private AppSharedPreference appSharedPreference;
    private NavigationView navigationView;
    private Fragment selectedFragement;
    ImageUploadReceiver imageUploadReceiver;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    LeedsFragment leedsFragement;
    public static String searchText = "";
    Menu menu;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appSharedPreference = new AppSharedPreference(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // NOTE : Just remove the fab button
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //NOTE:  Checks first item in the navigation drawer initially
        navigationView.setCheckedItem(R.id.generateleads);
        updateNavigationHeader();
        //NOTE:  Open fragment1 initially.
        selectedFragement = new GenerateLeedFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, selectedFragement);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        initMenu(false);
        return true;
    }

    private void initMenu(boolean isShow) {
        if (toolbar.getMenu() != null)
            toolbar.getMenu().clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        initSearchView(menu);
        clearSearchView(isShow);
    }

    public void initSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.item_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint("Name, Mobile Number, Alternet Number");
        if (searchManager != null) {
            SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
            searchView.setSearchableInfo(searchableInfo);
            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(this);
        }
    }

    private void clearSearchView(final boolean isShow) {
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchMenuItem.setVisible(isShow);
        if (isShow)
            searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        else
            searchView.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchText = newText;
        if (leedsFragement != null && leedsFragement.getLeedAdapter() != null)
            leedsFragement.getLeedAdapter().getFilter().filter(newText);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        //NOTE: creating fragment object
        Fragment fragment = null;
        if (id == R.id.generateleads) {
            fragment = new GenerateLeedFragment();
            initMenu(false);
        } else if (id == R.id.Leads) {
            leedsFragement = new LeedsFragment();
            fragment = leedsFragement;
            initMenu(true);
        } else if (id == R.id.Invices) {
            fragment = new InvoicesTabFragment();
            initMenu(false);
        } else if (id == R.id.Reports) {
            fragment = new ReportsFragment();
            initMenu(false);
        } else if (id == R.id.Calulator) {
            fragment = new CalculatorFragment();
            initMenu(false);
        } else if (id == R.id.item_logout) {
            clearDataWithSignOut();
            initMenu(false);
        }
        //NOTE: Fragment changing code
        selectedFragement = fragment;
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }
        //NOTE:  Closing the drawer after selecting
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); //Ya you can also globalize this variable :P
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String title) {
        // NOTE:  Code to replace the toolbar title based current visible fragment
        getSupportActionBar().setTitle(title);
    }

    private void clearDataWithSignOut() {
        FirebaseAuth.getInstance().signOut();
        appSharedPreference.clear();
        logOut();
    }

    private void logOut() {
        Intent intent = new Intent(this, LoginScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void updateNavigationHeader() {
        try {
            View header = navigationView.getHeaderView(0);
            TextView textViewAgentId = header.findViewById(R.id.textView_agent_id);
            TextView textViewUserName = header.findViewById(R.id.textView_user_name);
            TextView textViewEmailId = header.findViewById(R.id.text_view_email);
            TextView textViewMobileNumber = header.findViewById(R.id.textView_contact);
            final ImageView imageViewProfile = header.findViewById(R.id.image_view_profile);
            final ImageView ivProfileLayout = header.findViewById(R.id.ivProfileLayout);
            textViewUserName.setText(appSharedPreference.getUserName());
            textViewEmailId.setText(appSharedPreference.getEmaiId());
            textViewAgentId.setText(appSharedPreference.getAgeniId());
            textViewMobileNumber.setText(appSharedPreference.getMobileNo());
            if (!Utility.isEmptyOrNull(appSharedPreference.getProfileLargeImage())) {
                Picasso.with(this).load(appSharedPreference.getProfileLargeImage()).resize(200, 200).centerCrop().placeholder(R.drawable.imagelogo).into(imageViewProfile);
                Picasso.with(this)
                        .load(appSharedPreference.getProfileLargeImage())
                        .into(imageViewProfile, new Callback() {
                            @Override
                            public void onSuccess() {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bitmap innerBitmap = ((BitmapDrawable) imageViewProfile.getDrawable()).getBitmap();
                                        ivProfileLayout.setImageBitmap(Utility.blur(MainActivity.this, innerBitmap));
                                    }
                                }, 100);
                            }

                            @Override
                            public void onError() {
                            }
                        });
            } else {
                imageViewProfile.setImageResource(R.drawable.imagelogo);
                ivProfileLayout.setImageResource(0);
            }
            imageViewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, UpdateProfileActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
        } catch (Exception ex) {
            ExceptionUtil.logException(ex);
        }
    }

    @Override
    public void changeFragement(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            updateNavigationHeader();
        } else if (selectedFragement != null)
            selectedFragement.onActivityResult(requestCode, resultCode, data);
    }

    private void setReceiver() {
        try {
            IntentFilter filter = new IntentFilter(ImageUploadReceiver.PROCESS_RESPONSE);
            imageUploadReceiver = new ImageUploadReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(imageUploadReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        setReceiver();
        super.onStart();
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(imageUploadReceiver);
        super.onStop();
    }

    public class ImageUploadReceiver extends BroadcastReceiver {
        public static final String PROCESS_RESPONSE = "com.smartloan.smtrick.smart_loan.intent.action.UPDATE_USER_DATA";

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNavigationHeader();
        }
    }
}
