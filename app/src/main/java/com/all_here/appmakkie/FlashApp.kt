package com.all_here.appmakkie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.all_here.appmakkie.data.InternetItem
import com.all_here.appmakkie.ui.CartScreen
import com.all_here.appmakkie.ui.LoginUi
import com.all_here.appmakkie.ui.theme.FlashViewModel
import com.google.firebase.auth.FirebaseAuth

enum class flashAppScreen(val title: String){
    Start(" flashCart"),
    Items("Choose Items"),
    Cart("Your Cart")

}

var canNavigate = false
val auth = FirebaseAuth.getInstance()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun flashApp(flashViewModel: FlashViewModel = viewModel (),
             navController : NavHostController = rememberNavController()
){
    //firebase

    val user by flashViewModel.user.collectAsState()
    val logoutClicked by flashViewModel.logoutClicked.collectAsState()
    auth.currentUser?.let { flashViewModel.setUser(it) }
    val backStackEntry by navController.currentBackStackEntryAsState()

    //coroutine
    val isVisible by flashViewModel.isVisible.collectAsState()
    val currentScreen = flashAppScreen.valueOf(
        backStackEntry?.destination?.route?: flashAppScreen.Start.name
    )
    canNavigate = navController.previousBackStackEntry != null
    val cartItems by flashViewModel.cartitems.collectAsState()

    if (isVisible){
        OfferScreen()
    }else
        if (user == null){
       LoginUi(flashViewModel = flashViewModel)
    }
    else{
        Scaffold (
            topBar = {
                TopAppBar(title = {
                    Row (verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                        ){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentScreen.title,
                                fontSize = 26.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )

                            if (currentScreen == flashAppScreen.Cart) {
                                Text(
                                    text = "(${cartItems.size})",
                                    fontSize = 26.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                            }
                        }
                        Row (modifier = Modifier.clickable{
                            flashViewModel.setLogoutStatus(true)
//                            auth.signOut()
//                            flashViewModel.clearData()
                        }){
                            Icon(painter = painterResource(R.drawable.logout), contentDescription = "Logout",
                                modifier = Modifier.size(24.dp))
                            Text(text = "Logout", fontSize = 14.sp, modifier = Modifier.padding(start = 14.dp, end = 4.dp))

                        }
                    }

                },
                    navigationIcon = {
                        if(canNavigate){
                            IconButton(onClick = {
                                navController.navigateUp()
                            }) {
                                Icon(imageVector = Icons.Filled.ArrowBack,contentDescription ="Back Button")
                            }
                        }

                    }
                )
            },
            bottomBar = {
                flashAppBar(navController = navController,
                    currentScreen = currentScreen,
                    cartItems = cartItems
                    )
            },
        ){ innerPadding->
            NavHost(navController, flashAppScreen.Start.name)
//            NavHost(
//                navController = navController,
//                startDestination = flashAppScreen.Start.name,
//                modifier = Modifier.padding(innerPadding)
//            )
            {
                composable(route = flashAppScreen.Start.name
                ) {
                    firstScreen(flashViewModel = flashViewModel,
                        onCategoryClick = {
                            flashViewModel.updateSelectedCategory(it)
                            navController.navigate(flashAppScreen.Items.name)

                        },
                        modifier = Modifier.padding(innerPadding)
                    )


                }
                composable("cart") {
                    Text("CART OPENED")
                }

                composable(route = flashAppScreen.Items.name){
                    //change itemscreen to iis bcoz retrofit
                    InternetItemScreen(flashViewModel= flashViewModel,
                        itemUiState = flashViewModel.itemUistate
                    )
                }
                composable (route = flashAppScreen.Cart.name){
                    CartScreen(flashViewModel = flashViewModel,
                        onHomeButtonClicked = {
                            navController.navigate(flashAppScreen.Start.name){
                                popUpTo(0)
                            }
                        }
                        )
                }

            }
        }
        if (logoutClicked){
            AlertCheck(onYesButtonPressed = {
                flashViewModel.setLogoutStatus(false)
                auth.signOut()
                flashViewModel.clearData()
            },
                onNoButtonPressed = {
                    flashViewModel.setLogoutStatus(false)
                }

                )
        }
    }



}
//@Composable
//fun flashAppBar(
//    navController: NavHostController,
//    currentScreen: flashAppScreen,
//    cartItems: List<InternetItem>
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        horizontalArrangement = Arrangement.SpaceEvenly,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        // HOME BUTTON
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            IconButton(onClick = {
//                navController.navigate(flashAppScreen.Start.name) {
//                    launchSingleTop = true
//                }
//            }) {
//                Icon(
//                    imageVector = Icons.Outlined.Home,
//                    contentDescription = "Home"
//                )
//            }
//            Text(text = "Home", fontSize = 10.sp)
//        }
//
//        // CART BUTTON
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            IconButton(onClick = {
//                navController.navigate(flashAppScreen.Cart.name)
//            }) {
//                Box {
//                    Icon(
//                        imageVector = Icons.Outlined.ShoppingCart,
//                        contentDescription = "Cart"
//                    )
//                    if (cartItems.isNotEmpty()) {
//                        Card(
//                            modifier = Modifier.align(Alignment.TopEnd),
//                            colors = CardDefaults.cardColors(containerColor = Color.Red)
//                        ) {
//                            Text(
//                                text = cartItems.size.toString(),
//                                fontSize = 10.sp,
//                                color = Color.White,
//                                modifier = Modifier.padding(horizontal = 4.dp)
//                            )
//                        }
//                    }
//                }
//            }
//            Text(text = "Cart", fontSize = 10.sp)
//        }
//    }
//}




@Composable
fun flashAppBar (navController: NavHostController,
                 currentScreen : flashAppScreen,
                 cartItems: List<InternetItem>
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 70.dp,
                vertical = 10.dp
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable{
                navController.navigate(flashAppScreen.Start.name){
                    popUpTo(0)
                }
//                navController.navigate(flashAppScreen.Start.name) {
//                    launchSingleTop = true
//                }

            }
        ) {
            Icon(imageVector = Icons.Outlined.Home,
                contentDescription = "Home")
            Text(text = "Home", fontSize = 10.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable{
                if(currentScreen !=  flashAppScreen.Cart) {
                    navController.navigate(flashAppScreen.Cart.name){
                        popUpTo(0)
                    }

                }
            }
//            modifier = Modifier.clickable {
//                if (currentScreen != flashAppScreen.Cart) {
//                    navController.navigate(flashAppScreen.Cart.name)
//                }
//            }


        ) {
            Box {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Cart"
                )
                if(cartItems.isNotEmpty())
                Card(modifier = Modifier.align(
                    alignment = Alignment.TopEnd
                ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red
                    )
                    ) {
                   Text(text = cartItems.size.toString(),
                       fontSize = 10.sp,
                       color = Color.White,
                       fontWeight = FontWeight.ExtraBold,
                       modifier = Modifier.padding(horizontal = 1.dp)
                       )
                }
            }
            Text(text = "Cart", fontSize = 10.sp)
        }

    }
}

@Composable
fun AlertCheck(
    onYesButtonPressed :() -> Unit,
    onNoButtonPressed :() -> Unit
){
    AlertDialog(
        title = {
            Text(text = "Logout", fontWeight = FontWeight.Bold)
        },
        containerColor = Color.White,
        text = {
            Text(text = "Are yu sure you want to logout?")
        },
        confirmButton = {
            TextButton(onClick = {
                onYesButtonPressed()
            }) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onNoButtonPressed()
            }) {Text(text = "No") }
        },
        onDismissRequest = {
            onNoButtonPressed
        }
    )
}