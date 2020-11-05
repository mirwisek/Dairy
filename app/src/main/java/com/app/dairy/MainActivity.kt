package com.app.dairy

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

class MainActivity : AppCompatActivity() {

    private var isOnFirstFragment: Boolean = true
    var fabClickListener: OnFabClickedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)
        val controller = findNavController(R.id.nav_host_fragment)


        fab.setOnClickListener {
            isOnFirstFragment = when (isOnFirstFragment) {
                true -> {
                    controller.navigate(R.id.action_FirstFragment_to_SecondFragment)

                    fab.setImageDrawable(resources.getDrawable(R.drawable.ic_check, null))
                    false
                }
                false -> {

                    val result = try {
                        fabClickListener?.isSuccessfullyFilled() ?: true
                    } catch (e: Exception) {
                        Snackbar.make(it, "Please fill all the fields", Snackbar.LENGTH_SHORT).show()
                        e.printStackTrace()
                        false
                    }
                    if(result) {
                        controller.navigateUp()
                        true
                    } else
                        false
                }
            }
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

    interface OnFabClickedListener {
        fun isSuccessfullyFilled(): Boolean
    }
}