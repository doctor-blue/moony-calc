package com.moony.calc.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.card.MaterialCardView
import com.moony.calc.R
import com.moony.calc.base.BaseActivity
import com.moony.calc.fragments.SavingBoxFragment
import com.moony.calc.fragments.WalletFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity() {
    companion object {
        var isShowWalletFragment: Boolean = true
    }

    private var drawerToggle: ActionBarDrawerToggle? = null
    private lateinit var walletFragment: WalletFragment
    private lateinit var savingBoxFragment: SavingBoxFragment

    override fun init(savedInstanceState: Bundle?) {
        initControl()
        initEvent()
    }

    private fun initEvent() {
        card_saving_box.setOnClickListener {
            img_wallet.setImageResource(R.drawable.ic_wallet_no_color)
            img_saving_box.setImageResource(R.drawable.ic_piggy_color)
            if (isShowWalletFragment) {
                main_fragment.replaceFragment(savingBoxFragment, supportFragmentManager)
                isShowWalletFragment = false
                txt_choose_month_main.visibility=View.GONE
            }
        }
        card_wallet.setOnClickListener {
            img_saving_box.setImageResource(R.drawable.ic_piggy_no_color)
            img_wallet.setImageResource(R.drawable.ic_wallet_with_color)
            if (!isShowWalletFragment) {
                main_fragment.replaceFragment(walletFragment, supportFragmentManager)
                isShowWalletFragment = true
                txt_choose_month_main.visibility=View.VISIBLE
            }
        }
        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_chart->{
                    val intent=Intent(this@MainActivity,ChartActivity::class.java)
                    if (isShowWalletFragment)
                        intent.putExtra("isFinanceChart",true)
                    else
                        intent.putExtra("isFinanceChart",false)
                    startActivity(intent)
                }
            }
            true
        }
    }

    private fun initControl() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle!!)
        walletFragment = WalletFragment()
        savingBoxFragment = SavingBoxFragment()
        main_fragment.replaceFragment(walletFragment, supportFragmentManager)
    }


    override fun getLayoutId(): Int = R.layout.activity_main


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle!!.onConfigurationChanged(newConfig)
    }

}

