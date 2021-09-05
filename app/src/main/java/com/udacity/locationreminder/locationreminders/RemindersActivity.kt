package com.udacity.locationreminder.locationreminders

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.udacity.locationreminder.R
import com.udacity.locationreminder.authentication.AuthenticationActivity
import kotlinx.android.synthetic.main.activity_reminders.*

class RemindersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (nav_host_fragment as NavHostFragment).navController.popBackStack()
            }
            R.id.logout -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        AuthUI.getInstance().signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, AuthenticationActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Snackbar.make(
                    findViewById(R.id.nav_host_fragment),
                    "Unable to perform logout, please try again",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
    }
}
