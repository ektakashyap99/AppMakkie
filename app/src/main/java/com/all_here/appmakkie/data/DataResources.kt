package com.all_here.appmakkie.data
import androidx.annotation.StringRes
import com.all_here.appmakkie.R




object DataResources {
    fun loadCategories():List<Categories>{
        return listOf<Categories>(
            Categories(stringResourcesId = R.string.fresh_fruits, imageResourceId = R.drawable.freshfruits),
            Categories(stringResourcesId = R.string.bur_ger, imageResourceId = R.drawable.burger),
            Categories(stringResourcesId = R.string.ice_cream, imageResourceId = R.drawable.icecream),
            Categories(stringResourcesId = R.string.dry_fruits, imageResourceId = R.drawable.dry_fruits),
            Categories(stringResourcesId = R.string.bever_ages, imageResourceId = R.drawable.beverages),
            Categories(stringResourcesId = R.string.dess_erts, imageResourceId = R.drawable.desserts),

        )
    }
    fun loadItems(
        @StringRes  categoryName : Int
    ): List<Item>{
        return listOf(
            Item(R.string.a_apple, R.string.fresh_fruits,  100,"1kg",R.drawable.apple),
            Item(R.string.m_mango, R.string.fresh_fruits,  500,"3kg",R.drawable.apple),
            Item(R.string.b_banana, R.string.fresh_fruits,  200,"2kg",R.drawable.apple),
            Item(R.string.o_orange, R.string.fresh_fruits,  400,"3kg",R.drawable.apple),
            Item(R.string.d_drinks, R.string.bever_ages,  200,"50",R.drawable.apple)


        ).filter {
            it.itemCategoryId == categoryName
        }
    }
}