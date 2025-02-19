package com.example.assembee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikhaellopez.circularimageview.CircularImageView


class MainActivity : AppCompatActivity() {
    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                val dashFragment = DashboardFragment.newInstance()
                setCurrentFragment(dashFragment)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_explore -> {
                val exploreFragment = ExploreFragment.newInstance()
                setCurrentFragment(exploreFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val notifFragment = notif_dummy.newInstance()
                setCurrentFragment(notifFragment)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_likes -> {
                val likesFragment = LikesFragment.newInstance()
                setCurrentFragment(likesFragment)

                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCurrentFragment(DashboardFragment.newInstance())
        setContentView(R.layout.activity_main)
        val bottom_nav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottom_nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val button_add_post: ImageButton = findViewById(R.id.addpost)
        button_add_post.setOnClickListener {
            val intent = Intent(this, Add_Post :: class.java)
            startActivity(intent)
        }

        // log-in button listener
        val profile_button: CircularImageView = findViewById(R.id.profile);
        profile_button.setOnClickListener {
            val signin_intent = Intent(this, Signin::class.java)
            startActivity(signin_intent)
        }

        // display logged in user's profile

        // get saved userId
        val sh = getSharedPreferences(
            "sharedPref",
            Context.MODE_PRIVATE
        )

        val avatarUrl = sh.getString("avatarURL", "")

        if (avatarUrl != "") {
            Glide.with(this)
                .load(avatarUrl)
                .into(profile_button)
        }

    }

    override fun onResume() {
        super.onResume()
        // display logged in user's profile
        val profile_button: CircularImageView = findViewById(R.id.profile);

        // get saved userId
        val sh = getSharedPreferences(
            "sharedPref",
            Context.MODE_PRIVATE
        )

        val avatarUrl = sh.getString("avatarURL", "")

        if (avatarUrl != "") {
            Glide.with(this)
                .load(avatarUrl)
                .into(profile_button)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}