package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.SupportViews.HomePageFragment;
import com.example.myapplication.R;
import com.example.myapplication.SessionManager.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    SessionManager sessionManager;
    TextView navProfileName, navProfileMail;
    MenuItem navLogin, navLogout, navRegister;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        updateNavHeader();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomePageFragment()).commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.nav_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.nav_logout:
                Toast.makeText(HomePageActivity.this,"User logged out.", Toast.LENGTH_LONG).show();
                sessionManager.logoutUserFromSession();
                updateNavHeader();

                break;
            default:
                return false;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void updateNavHeader() {

        View headerView = navigationView.getHeaderView(0);

        navProfileName = headerView.findViewById(R.id.nav_profileName);
        navProfileMail = headerView.findViewById(R.id.nav_profileMail);

        Menu drawerView = navigationView.getMenu();

        navLogin = drawerView.findItem(R.id.nav_login);
        navRegister = drawerView.findItem(R.id.nav_register);
        navLogout = drawerView.findItem(R.id.nav_logout);

        if (!sessionManager.checkLogin()) {
            navProfileName.setText("Username");
            navProfileMail.setText("Email address");
            navLogin.setVisible(true);
            navRegister.setVisible(true);
            navLogout.setVisible(false);
        } else {
            navProfileName.setText(sessionManager.getUserDetailsFromSession().get(SessionManager.KEY_NAME) + " " + sessionManager.getUserDetailsFromSession().get(SessionManager.KEY_SURNAME));
            navProfileMail.setText(sessionManager.getUserDetailsFromSession().get(SessionManager.KEY_EMAIL));
            navLogin.setVisible(false);
            navRegister.setVisible(false);
            navLogout.setVisible(true);
        }



    }

    private void updateDrawerMenu(){

        Menu drawerView = navigationView.getMenu();

        navLogin = drawerView.findItem(R.id.nav_login);
        navRegister = drawerView.findItem(R.id.nav_register);
        navLogout = drawerView.findItem(R.id.nav_logout);

        if (!sessionManager.checkLogin()) {

            navLogin.setVisible(true);
            navRegister.setVisible(true);
            navLogout.setVisible(false);

        } else {
            navLogin.setVisible(false);
            navRegister.setVisible(false);
            navLogout.setVisible(true);
        }


    }

    public void postLogoutDrawerMenu(){

        Menu drawerView = navigationView.getMenu();

        navLogin = drawerView.findItem(R.id.nav_login);
        navRegister = drawerView.findItem(R.id.nav_register);
        navLogout = drawerView.findItem(R.id.nav_logout);

        navLogin.setVisible(true);
        navRegister.setVisible(true);
        navLogout.setVisible(false);
    }


}