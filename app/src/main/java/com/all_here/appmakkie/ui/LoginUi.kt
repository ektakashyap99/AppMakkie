package com.all_here.appmakkie.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.all_here.appmakkie.ui.theme.FlashViewModel
import com.all_here.appmakkie.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider


@Composable
fun LoginUi(flashViewModel: FlashViewModel) {
    val context = LocalContext.current
    val otp by flashViewModel.otp.collectAsState()
    val verificationId by flashViewModel.verificationId.collectAsState()
    val loading by flashViewModel.loading.collectAsState()


    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Show the error and stop the loader
            Toast.makeText(context, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("PhoneAuth", "onVerificationFailed", e)
            flashViewModel.setLoading(false)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            flashViewModel.setVerificationId(verificationId)
            Toast.makeText(context, "OTP sent", Toast.LENGTH_SHORT).show()
            flashViewModel.resetTimer()
            flashViewModel.runTimer()
            flashViewModel.setLoading(false)
        }
    }


    Box (){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.screenshot_2025_10_05_171218), contentDescription = "App Logo",
                modifier = Modifier
                    .padding(
                        top = 50.dp,
                        bottom = 10.dp
                    )
                    .size(100.dp)
            )

            if (verificationId.isEmpty()) {
                            NumberScreen(flashViewModel = flashViewModel, callbacks = callbacks)


            } else {

                            OtpScreen(otp = otp, flashViewModel = flashViewModel,callbacks = callbacks)
            }


        }
        // replace
        if (verificationId.isEmpty()){
            IconButton(onClick = {
                flashViewModel.setVerificationId("")
                flashViewModel.setOtp("")
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }

// with
        if (verificationId.isNotEmpty()){
            IconButton(onClick = {
                flashViewModel.setVerificationId("")
                flashViewModel.setOtp("")
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }



    }



}