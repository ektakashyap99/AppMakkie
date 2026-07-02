package com.all_here.appmakkie

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.all_here.appmakkie.data.DataResources
import com.all_here.appmakkie.ui.theme.FlashViewModel


@Composable
fun firstScreen(
    flashViewModel: FlashViewModel,
    onCategoryClick: (Int) -> Unit,
    modifier: Modifier
){
    val context = LocalContext.current
    val flashUistate by flashViewModel.uiState.collectAsState()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                        containerColor = Color(100,255,100, 255)
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Text(text = " Shop by Category",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp)
                        )

                }
            }

        }
        items(DataResources.loadCategories()){
            CategoryCard(
                context = context,
                stringResourcesId = it.stringResourcesId,
                imageResourceId = it.imageResourceId,
                flashViewModel = flashViewModel,
                onCategoryClick = onCategoryClick
            )
        }
    }
}



@Composable
fun CategoryCard(
    context: Context,
    stringResourcesId: Int,
    imageResourceId: Int,
    flashViewModel: FlashViewModel,
    onCategoryClick :(Int)-> Unit


){
    val categoryName = stringResource(id = stringResourcesId)
    Card(modifier = Modifier.aspectRatio(1f).clickable{
        flashViewModel.updateClickText(categoryName)
        Toast.makeText(context, "Clicked $categoryName",Toast.LENGTH_SHORT).show()
        onCategoryClick(stringResourcesId)
    },
        colors = CardDefaults.cardColors(
            containerColor = Color(240,221,240, 255)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
        ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp),
//                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = categoryName,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Image(painter = painterResource(imageResourceId), contentDescription = stringResource(id = stringResourcesId),
                modifier = Modifier
//                    .size(150.dp)
                    .fillMaxWidth()
                    .weight(1f)
            )


        }


    }

}






