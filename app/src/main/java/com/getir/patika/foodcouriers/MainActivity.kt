package com.getir.patika.foodcouriers

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*

        tabLayout = findViewById(R.id.tab_account)
        viewPager2 = findViewById(R.id.viewpager_account)
        pagerAdapter = PagerAdapter(supportFragmentManager,lifecycle).apply {
            addFragment(CreateAccountFragment())
            addFragment(LoginAccountFragment())
        }
        viewPager2.adapter = pagerAdapter

        TabLayoutMediator(tabLayout,viewPager2){ tab, position ->
             when(position) {
                 0 -> {
                     tab.text = "Create Account"
                 }
                 1 -> {
                     tab.text = "Login Account"
                 }
             }

        }.attach()
         */

    }
}