package com.example.a22a1001d0098_trantruonggiang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a22a1001d0098_trantruonggiang.data.FoodDatabase
import com.example.a22a1001d0098_trantruonggiang.model.Food
import com.example.a22a1001d0098_trantruonggiang.ui.theme._22a1001d0098_TranTruongGiangTheme
import com.example.a22a1001d0098_trantruonggiang.userinterface.FoodAddScreen
import com.example.a22a1001d0098_trantruonggiang.userinterface.FoodEditScreen
import com.example.a22a1001d0098_trantruonggiang.userinterface.FoodListScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = FoodDatabase.getDatabase(this)
        val foodDao = db.foodDao()
        runBlocking {
            val foods = foodDao.getAllFoods().first() // Lấy danh sách hiện tại
            if (foods.isEmpty()) {
                val sampleFoods = listOf(
                    Food(name = "Gỏi gà", price = 120000, unit = "đồng", imageUrl = "https://picsum.photos/80"),
                    Food(name = "Bò lúc lắc", price = 150000, unit = "đồng", imageUrl = "https://picsum.photos/81"),
                    Food(name = "Tôm háp", price = 100000, unit = "đồng", imageUrl = "https://picsum.photos/82"),
                    Food(name = "Súp cua", price = 80000, unit = "đồng", imageUrl = "https://picsum.photos/83"),
                    Food(name = "Lẩu hải sản", price = 200000, unit = "đồng", imageUrl = "https://picsum.photos/84")
                )
                sampleFoods.forEach { food ->
                    CoroutineScope(Dispatchers.IO).launch {
                        foodDao.insertFood(food)
                    }
                }
            }
        }

        setContent {
            _22a1001d0098_TranTruongGiangTheme {
                val navController = rememberNavController()
                val db = FoodDatabase.getDatabase(this)
                NavHost(navController, startDestination = "food_list") {
                    composable("food_list") { FoodListScreen(navController, db) }
                    composable("edit_food/{foodId}") { backStackEntry ->
                        val foodId = backStackEntry.arguments?.getString("foodId")?.toInt() ?: 0
                        FoodEditScreen(navController, foodId, db)
                    }
                    composable("add_food") { FoodAddScreen(navController, db) }
                }
            }
        }
    }
}

