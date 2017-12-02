package com.rupiee.android.mfi.user;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rupiee.android.R;

/**
 * This is a super class for all activities which need to display a navigational drawer.
 * The content of the menu can be edited in {@link res/menu/drawer_view.xml} file.
 * If any activity needs to show the navigational drawer, that activity class needs to
 * extend this class
 *
 * @author ketan
 * @version 1.0.3
 * @since 09/05/16
 */
public class UserDrawerActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;


    /**
     * This is the main method in this class. To setup a drawer,
     * the subclass should call this method in it while setting up its views
     */
    protected void setupDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.reguser_drawer);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(nvDrawer);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    Fragment mCurrentFragment;
    /**
     * This method sets the functionality for the menuitems provided in {@link res/menu/drawer_view.xml} file
     *
     * @param menuItem Platform POJO for menu item
     */
    private void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_mfiuser_form:
                RegisterUserFragment registerUserFragment = RegisterUserFragment.newInstance();
                mCurrentFragment = registerUserFragment;
                break;
            case R.id.nav_mfiuser_list:
                UserListFragment userListFragment = UserListFragment.newInstance();
                mCurrentFragment = userListFragment;
                break;
        }

        replaceFragment(R.id.rua_fragcontainer, mCurrentFragment);
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu.findItem(R.id.menuId) != null){
            menu.findItem(R.id.menuId).setTitle("1");
        };
        return super.onPrepareOptionsMenu(menu);
    }

    public void changeMenuTitle(String menuTitle) {
        invalidateOptionsMenu();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Util method to add a new fragment by adding the current fragment to the stack
     *
     * @param viewId View Id of the view where the fragment needs to be populated
     * @param fragment The new fragment which needs to be displayed
     */
    protected void addFragment(int viewId, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(viewId, fragment)
                .commit();
        mCurrentFragment = fragment;
    }

    /**
     * Util method to add a new fragment by replacing the current fragment
     *
     * @param viewId View Id of the view where the fragment needs to be populated
     * @param fragment The new fragment which needs to be displayed
     */
    public void replaceFragment(int viewId, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(viewId, fragment)
                .commit();
        mCurrentFragment = fragment;
    }

}
