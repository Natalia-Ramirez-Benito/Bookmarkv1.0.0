package com.example.bookmark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bookmark.ui.AddFragment
import com.example.bookmark.ui.HomeFragment
import com.example.bookmark.ui.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Menu : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                // HomeFragment
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                // AddFragment
                R.id.bottom_add -> {
                    replaceFragment(AddFragment())
                    true
                }
                // ProfileFragment
                R.id.bottom_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        replaceFragment(HomeFragment())

    }


    private fun replaceFragment(fragment: Fragment) {
        // Obtener el nombre de usuario del Intent
        val username = intent.getStringExtra("username") ?: ""

        // Pasar el nombre de usuario al fragmento utilizando el método newInstance
        val newFragment = when (fragment) {
            is HomeFragment -> HomeFragment.newInstance(username)
            is AddFragment -> AddFragment.newInstance(username)
            is ProfileFragment -> ProfileFragment.newInstance(username)
            else -> fragment
        }
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, newFragment).commit()
    }

}