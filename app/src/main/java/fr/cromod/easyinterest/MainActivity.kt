package fr.cromod.easyinterest

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private fun loadFragment(fragment: Fragment?, title: Int)
    {
        if (fragment == null) return

        if (!fragment.isVisible)
        {
            setTitle(getString(title))
            supportFragmentManager.beginTransaction().replace(fragment_to_display.id, fragment).commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        loadFragment(CompoundInterestFragment.newInstance(), R.string.compound_interest)

        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "TO DEFINE", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed()
    {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
        {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else
        {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.compound_interest -> loadFragment(CompoundInterestFragment.newInstance(), R.string.compound_interest)
            R.id.property_loan -> loadFragment(PropertyLoanFragment.newInstance(), R.string.property_loan)
            R.id.loan_prepayment -> loadFragment(LoanPrepaymentFragment.newInstance(), R.string.loan_prepayment)
            R.id.loan_repurchase -> loadFragment(LoanRepurchaseFragment.newInstance(), R.string.loan_repurchase)
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
