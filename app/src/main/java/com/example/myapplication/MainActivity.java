package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.WatchList.WatchlistViewModel;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.LoginFragment;
import com.example.myapplication.fragment.MapFragment;
import com.example.myapplication.fragment.MemoirFragment;
import com.example.myapplication.fragment.ReportFragment;
import com.example.myapplication.fragment.SearchFragment;
import com.example.myapplication.fragment.WatchListFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    public WatchlistViewModel watchlistViewModel;

    //Configuration options for NavigationUI methods
// that interact with implementations of the app bar pattern such as Toolbar, CollapsingToolbarLayout, and ActionBar.
    private AppBarConfiguration mAppBarConfiguration;
    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set ViewModel
        watchlistViewModel = ViewModelProviders.of(this).get(WatchlistViewModel.class);
        watchlistViewModel.initalizeVars(getApplication());
        //adding the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar); setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //these two lines of code show the navicon drawer icon top left hand side
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new LoginFragment());}


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                replaceFragment(new HomeFragment());
                break;
            case R.id.nav_search:
                replaceFragment(new SearchFragment()); break;
            case R.id.nav_memoir:
                replaceFragment(new MemoirFragment()); break;
            case R.id.nav_watchlist:
                replaceFragment(new WatchListFragment()); break;
            case R.id.nav_report:
                replaceFragment(new ReportFragment()); break;
            case R.id.nav_map:
                replaceFragment(new MapFragment()); break;
            case R.id.nav_login:
                replaceFragment(new LoginFragment()); break;
        }
        //this code closes the drawer after you selected an item from the menu,otherwise stay open
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { if (toggle.onOptionsItemSelected(item))
        return true;
        return super.onOptionsItemSelected(item);
    }

}
