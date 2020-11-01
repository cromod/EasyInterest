package fr.cromod.easyinterest

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import fr.cromod.easyinterest.views.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.selector
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
    var currentFragment: AbstractFragment? = CompoundInterestFragment.newInstance()
    var currentTitle: Int = R.string.compound_interest

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setLocale(getPreference("language"), false)
        initializeFragment()
        loadFragment()

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
        currentFragment?.saveInputs()
        
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

    private fun initializeFragment()
    {
        if (PropertyLoanFragment.newInstance()!!.enabled)
        {
            currentFragment = PropertyLoanFragment.newInstance()
            currentTitle = R.string.property_loan
        }
        else if (LoanPrepaymentFragment.newInstance()!!.enabled)
        {
            currentFragment = LoanPrepaymentFragment.newInstance()
            currentTitle = R.string.loan_prepayment
        }
        else if (LoanRepurchaseFragment.newInstance()!!.enabled)
        {
            currentFragment = LoanRepurchaseFragment.newInstance()
            currentTitle = R.string.loan_repurchase
        }
        else
        {
            // Default fragment
            currentFragment = CompoundInterestFragment.newInstance()
            currentTitle = R.string.compound_interest
        }
    }

    private fun loadFragment(fragment: AbstractFragment? = currentFragment, title: Int = currentTitle)
    {
        if (fragment == null) return

        currentFragment?.enabled = false
        fragment?.enabled = true

        currentFragment = fragment
        currentTitle = title

        if (!currentFragment!!.isVisible)
        {
            setTitle(getString(currentTitle))
            supportFragmentManager.beginTransaction().replace(fragment_to_display.id, currentFragment!!).commit()
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
        val languages = listOf(
            getString(R.string.english),
            getString(R.string.french),
            getString(R.string.chinese)
        )
        selector(getString(R.string.choose_language), languages) { _, i ->
            when(languages[i])
            {
                getString(R.string.french) -> setLocale("fr")
                getString(R.string.english) -> setLocale("en")
                getString(R.string.chinese) -> setLocale("zh")
            }
        }

    }

    private fun setLocale(localeName: String, restart: Boolean = true)
    {
        if (localeName == "") return

        // Set selected language
        val config = resources.configuration
        val locale = Locale(localeName)
        Locale.setDefault(locale)
        if (locale == config.locale) return
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        // Save this locale value as a preference
        setPreference("language", localeName)

        // Restart activity and reload fragment
        if (!restart) return
        if (currentFragment != null)
        {
            currentFragment?.saveInputs()
            supportFragmentManager.beginTransaction().remove(currentFragment!!).commit()
        }
        finish()
        startActivity(intent)
    }

    private fun getPreference(key: String): String
    {
        val sharedPreference = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        return sharedPreference.getString(key, "") ?: ""
    }

    private fun setPreference(key: String, value: String)
    {
        val sharedPreference = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(key, value).commit()
    }
}
