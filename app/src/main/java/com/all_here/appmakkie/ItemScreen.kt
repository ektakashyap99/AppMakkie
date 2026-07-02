package com.all_here.appmakkie

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.all_here.appmakkie.data.InternetItem
import com.all_here.appmakkie.ui.theme.FlashViewModel




@Composable
fun ItemScreen(flashViewModel: FlashViewModel,
               items : List<InternetItem>
){
    val flashUistate by flashViewModel.uiState.collectAsState()
    val selectedCategory = stringResource(id = flashUistate.selectedCategory)
    val database = items
//        .filter {
//        it.itemCategory.lowercase() == selectedCategory.lowercase()
//    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item(
            span = {
                GridItemSpan(2)
            }
        ){
            Column {
                Image(painter = painterResource(id = R.drawable.cartbanner), contentDescription = "offer")
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(108,194,111, 255)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Text(text = "${stringResource(id = flashUistate.selectedCategory)}(${database.size})" ,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                }
            }

        }
        items(database){
            ItemCard(
                stringResourceId = it.itemName,
                imageResourceId = it.imageURL,
                itemPriceId = it.itemPrice,
                itemQuantityId = it.itemQuantity,
                flashViewModel = flashViewModel

            )
        }

    }
}
@Composable
fun InternetItemScreen(flashViewModel: FlashViewModel,
                       itemUiState: FlashViewModel.ItemUiState
){
    //uiState
    when(itemUiState){
        is FlashViewModel.ItemUiState.Loading -> {
            LoadingScreen()
        }
        is FlashViewModel.ItemUiState.Success -> {
//            Text(text = itemUiState.items.toString())
            ItemScreen(flashViewModel = flashViewModel, items = itemUiState.items)
        }
        else -> {ErrorScreen(flashViewModel = flashViewModel)}

    }
//    val itemUistate :String  = flashViewModel.itemUistate
//    Text(text = itemUistate)
}
@Composable
fun ItemCard(
    stringResourceId: String,
    imageResourceId: String,
    itemPriceId: Int,
    itemQuantityId: String,
    flashViewModel: FlashViewModel
){
    val context = LocalContext.current
    Column(modifier = Modifier.width(150.dp)
    ){
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(240,221,240, 255)
            ),
        ) {
            Box {
                AsyncImage(model = imageResourceId, contentDescription = stringResourceId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                )
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End
                ){
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(244,67,54,255)
                        )
                    ) {
                        Text(
                            text= "25% Off",
                            color = Color.White,
                            fontSize = 8.sp,
                            modifier = Modifier.padding(
                                horizontal = 5.dp,
                                vertical = 2.dp
                            )
                        )


                    }
                }
            }

        }
        Text(
            text = stringResourceId,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            maxLines = 1,
            textAlign = TextAlign.Left

        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row {
                    Text(
                        text = "Rs. $itemPriceId",
                        fontSize = 6.sp,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        color = Color(109, 109, 109, 255),
                        textDecoration = TextDecoration.LineThrough
                    )
                    Spacer(modifier = Modifier.width(80.dp))
                    Text(
                    text = itemQuantityId,
                    fontSize = 14.sp,
                    maxLines = 1,
                    color = Color(114, 114, 114, 255)

                )
                }
                Text(
                    text = "Rs. ${itemPriceId*75/100}",
                    fontSize = 10.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = Color(255, 116, 105, 255)

                )
                Card (modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        flashViewModel.addToDatabase(
                            InternetItem(
                                itemName = stringResourceId,
                                itemQuantity = itemQuantityId,
                                itemPrice = itemPriceId,
                                imageURL = imageResourceId,
                                itemCategory = ""


                            )
                        )
                        Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(108, 194, 111,255)
                    )

                ){
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Add to Cart",
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }

        }

    }
}

@Composable
fun LoadingScreen(){
    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Image(painter = painterResource(R.drawable.apple), contentDescription = "Loading")
    }
}

@Composable
fun ErrorScreen(flashViewModel: FlashViewModel){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()){
        Image(painter = painterResource(R.drawable.apple), contentDescription = "Error")
        Text(text = " OOp's! Internet unvailable , Please check your connnection or retry again",
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = {flashViewModel.getFlashItems()}) {
            Text(text = "Retry")
        }
    }

}