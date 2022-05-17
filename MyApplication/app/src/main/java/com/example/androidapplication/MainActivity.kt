package com.example.androidapplication

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {


    lateinit var toggle :ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //shareSheet
//        var openShareSheet = findViewById<Button>(R.id.nav_share)
//        openShareSheet.setOnClickListener{
//            shareMethod()
//        }

        //navigationDrawer
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {

            it.isChecked = true
            val intent = Intent(this, RecyclerView::class.java)
            val newIntent = Intent(this, CameraActivity::class.java)
            when(it.itemId){
                R.id.nav_camera -> startActivity(newIntent)
                R.id.nav_profile ->  replaceFragment(ProfileFragment(), it.title.toString())
                R.id.nav_task ->  startActivity(intent)
                R.id.nav_share ->  shareMethod()
                R.id.nav_settings -> replaceFragment(SettingsFragment(), it.title.toString())

            }
            true

        }
    }

    private fun replaceFragment(fragment: Fragment, title: String){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun shareMethod(){
        var message = "Hi! \n\n"+
                "This message was sent from my app!"
        var intent = Intent()
        intent.type = "text/plain"
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT,message)
        startActivity(intent)
    }

}