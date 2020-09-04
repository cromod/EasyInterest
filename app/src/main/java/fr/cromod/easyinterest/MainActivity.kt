package fr.cromod.easyinterest

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
import org.jetbrains.anko.selector
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
    lateinit var currentFragment: Fragment
    var currentTitle: Int = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        loadFragment(CompoundInterestFragment.newInstance(), R.string.compound_interest)

        setSupportActionBar(toolbar)

        // TO ENABLE WHEN COMPLETELY IMPLEMENTED
        /*val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "TO DEFINE", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        // TO ENABLE WHEN COMPLETELY IMPLEMENTED
        //menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.action_save -> return true
            R.id.action_history -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.nav_compound_interest -> loadFragment(
                CompoundInterestFragment.newInstance(),
                R.string.compound_interest
            )
            R.id.nav_property_loan -> loadFragment(
                PropertyLoanFragment.newInstance(),
                R.string.property_loan
            )
            R.id.nav_loan_prepayment -> loadFragment(
                LoanPrepaymentFragment.newInstance(),
                R.string.loan_prepayment
            )
            R.id.nav_loan_repurchase -> loadFragment(
                LoanRepurchaseFragment.newInstance(),
                R.string.loan_repurchase
            )
            R.id.nav_languages -> chooseLanguage()
            R.id.nav_rate -> rateApp()
            R.id.nav_share -> shareApp()
            R.id.nav_email_us -> sendEmail()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(fragment: Fragment? = currentFragment, title: Int = currentTitle)
    {
        if (fragment == null) return
        currentFragment = fragment
        currentTitle = title

        if (!currentFragment.isVisible)
        {
            setTitle(getString(currentTitle))
            supportFragmentManager.beginTransaction().replace(fragment_to_display.id, currentFragment).commit()
        }
    }

    private fun shareApp()
    {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_this_app))
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_to)))
    }

    private fun sendEmail()
    {
        val mailIntent = Intent(Intent.ACTION_SENDTO)
        mailIntent.data = Uri.parse("mailto:" + getString(R.string.app_email))
        startActivity(mailIntent)
    }

    private fun rateApp()
    {
        val rateIntent = Intent(Intent.ACTION_VIEW)
        val appPackageName = "fr.cromod.easyinterest"

        try
        {
            rateIntent.data = Uri.parse("market://details?id=$appPackageName")
            startActivity(rateIntent)
        }
        catch (e: ActivityNotFoundException)
        {
            rateIntent.data = Uri.parse("http://play.google.com/store/apps/details?id=$appPackageName")
            startActivity(rateIntent)
        }
    }

    private fun chooseLanguage()
    {
        val languages = listOf(getString(R.string.french), getString(R.string.english))
        selector(getString(R.string.choose_language), languages) { _, i ->
            when(languages[i])
            {
                getString(R.string.french) -> setLocale("fr")
                getString(R.string.english) -> setLocale("en")
            }
        }

    }

    private fun setLocale(localeName: String)
    {
        // Set selected language
        val config = resources.configuration
        val locale = Locale(localeName)
        Locale.setDefault(locale)
        if (locale == config.locale) return
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        // Restart activity and reload fragment
        supportFragmentManager.beginTransaction().remove(currentFragment).commit()
        finish()
        startActivity(intent)
        loadFragment()
    }
}
