package com.all_here.appmakkie.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.all_here.appmakkie.auth
import com.all_here.appmakkie.data.InternetItem
import com.all_here.appmakkie.network.FlashApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.DatabaseReference




class FlashViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(FlashUistate())
    val uiState: StateFlow<FlashUistate> = _uiState.asStateFlow()

    //coroutine
    val _isVisible = MutableStateFlow<Boolean>(true)
    val isVisible = _isVisible

    lateinit var internetJob : Job
    lateinit var screenJob : Job

    var itemUistate : ItemUiState  by mutableStateOf(ItemUiState.Loading)
        private set

    //firebase
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: MutableStateFlow<FirebaseUser?> get() = _user

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: MutableStateFlow<String> get() = _phoneNumber

    private val _otp = MutableStateFlow("")
    val otp : MutableStateFlow<String> get() = _otp

    private val _verificationId = MutableStateFlow("")
    val verificationId : MutableStateFlow<String> get() = _verificationId

    private val _ticks = MutableStateFlow(60L)
    val ticks : MutableStateFlow<Long> get() = _ticks

    private val _loading= MutableStateFlow(false)
    val loading : MutableStateFlow<Boolean> get() = _loading

    private val _logoutClicked= MutableStateFlow(false)
    val logoutClicked : MutableStateFlow<Boolean> get() = _logoutClicked

    private lateinit var timerJob: Job

    val database = Firebase.database
    val myRef = database.getReference("users/${auth.currentUser?.uid}/cart")
//      lateinit var myRef: DatabaseReference


    private  val _cartItems = MutableStateFlow<List<InternetItem>>(emptyList())
    val cartitems: StateFlow<List<InternetItem>> get() = _cartItems.asStateFlow()


    private  val  cartItemsKey = stringPreferencesKey("cart_items")




    sealed interface ItemUiState{
        data class Success(val items:List<InternetItem>) : ItemUiState
        object  Loading : ItemUiState
        object  Error : ItemUiState
    }

    fun setVerificationId(verificationId: String){
        _verificationId.value = verificationId

    }
    fun setOtp(otp: String){
       _otp.value = otp

    }
    fun setPhoneNumber(phoneNumber: String){
        _phoneNumber.value = phoneNumber

    }
    fun setUser(user: FirebaseUser?){
        _user.value = user
    }

//    fun setUser(user: FirebaseUser?) {
//        _user.value = user
//        user?.let {
//            myRef = Firebase.database
//                .getReference("users/${it.uid}/cart")
//            fillCartItems()   // 🔥 start listening AFTER login
//        }
//    }


    fun clearData(){
        _user.value = null
        _phoneNumber.value = ""
        _otp.value = ""
        _verificationId.value = ""

    }


    fun runTimer(){
        timerJob = viewModelScope.launch {
            while (_ticks.value > 0 ){
                delay(1000)
                _ticks.value -= 1
            }
        }
    }

    fun resetTimer(){
        try {
            if (::timerJob.isInitialized) timerJob.cancel()
        }catch (exception: Exception){
            // ignore
        } finally {
            _ticks.value = 60
        }
    }


    fun setLoading(loading: Boolean){
        _loading.value = loading
    }

    fun setLogoutStatus(logoutClicked: Boolean){
        _logoutClicked.value = logoutClicked
    }


    fun addToCart(item: InternetItem){
        _cartItems.value = _cartItems.value + item

    }

    fun addToDatabase(item: InternetItem){
        myRef.push().setValue(item)

    }

    fun fillCartItems(){

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _cartItems.value = emptyList()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(InternetItem::class.java)
                    item?.let { addToCart(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

    }



    fun removeFromCart(oldItem: InternetItem){
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override  fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children){
                    val item = childSnapshot.getValue(InternetItem::class.java)
                    var itemRemoved = false
                    item?.let {
                        if (oldItem.itemName == it.itemName && oldItem.itemPrice == it.itemPrice){
                            childSnapshot.ref.removeValue()
                            itemRemoved = true
                        }

                    }
                    if (itemRemoved) break

                }

            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
    fun updateClickText(updatedText:String){
        _uiState.update{
        it.copy(
                clickStatus = updatedText
            )
        }
    }


    fun toggleVisibilty(){
        _isVisible.value = false
    }

    fun getFlashItems(){
        internetJob = viewModelScope.launch{
            try {
                val listResult = FlashApi.retroService.getItems()
                itemUistate = ItemUiState.Success(listResult)
            }
            catch (exception: Exception){
                itemUistate = ItemUiState.Error
                toggleVisibilty()
                screenJob.cancel()
            }
        }
    }
    fun updateSelectedCategory(updatedCategory: Int){
        _uiState.update{
            it.copy(
                selectedCategory = updatedCategory
            )
        }

    }
    init {
        screenJob = viewModelScope.launch(Dispatchers.Default) {
            delay(2000)
            toggleVisibilty()
        }
        getFlashItems()
        fillCartItems()
    }
//init {
//    screenJob = viewModelScope.launch(Dispatchers.Default) {
//        delay(2000)
//        toggleVisibilty()
//    }
//    getFlashItems()
//}


}






