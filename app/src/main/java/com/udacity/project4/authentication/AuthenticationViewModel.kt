package com.udacity.project4.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.udacity.project4.utils.FirebaseUserLiveData

class AuthenticationViewModel : ViewModel() {

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }
}
