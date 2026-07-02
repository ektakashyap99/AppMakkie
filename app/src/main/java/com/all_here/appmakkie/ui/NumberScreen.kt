package com.all_here.appmakkie.ui
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.all_here.appmakkie.auth
import com.all_here.appmakkie.ui.theme.FlashViewModel
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
@Composable fun NumberScreen(flashViewModel: FlashViewModel,
                             callbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks ) {
    val phoneNumber by flashViewModel.phoneNumber.collectAsState()
    val context = LocalContext.current
    Text(text = "Login", fontSize = 24.sp, fontWeight = FontWeight.Bold, )
    Text(text = "Enter your phone number to proceed", modifier = Modifier.fillMaxWidth(), fontSize = 18.sp, )
    Text(text = "This phone number will be used for the purpose of all communication. yo shall receive an SMS with a code for viewers ", fontSize = 12.sp, color = Color.Gray )
    TextField( value = phoneNumber, keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Number ), onValueChange = { flashViewModel.setPhoneNumber(it) }, label = { Text(text = "Your number") }, modifier = Modifier.fillMaxWidth(), singleLine = true )

    Button(
        onClick = {
            val raw = phoneNumber.trim()
            // Basic validation: 10 digits only (adjust for your UX)
            if (raw.length != 10 || !raw.all { it.isDigit() }) {
                Toast.makeText(context, "Enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show()
                return@Button
            }
            flashViewModel.setLoading(true)
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91$raw")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(context as Activity)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Send OTP")
    }

}