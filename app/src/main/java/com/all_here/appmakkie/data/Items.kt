package com.all_here.appmakkie.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Item (
    @StringRes val stringResourceId : Int,
    @StringRes val itemCategoryId : Int,
    val itemPriceId: Int,
    val itemQuantityId : String,
   @DrawableRes val imageResourceId : Int
)