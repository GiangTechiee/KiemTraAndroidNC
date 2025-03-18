package com.example.a22a1001d0098_trantruonggiang.userinterface

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.a22a1001d0098_trantruonggiang.data.FoodDatabase
import com.example.a22a1001d0098_trantruonggiang.model.Food
import com.example.a22a1001d0098_trantruonggiang.ui.theme._22a1001d0098_TranTruongGiangTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodEditScreen(navController: NavHostController, foodId: Int, db: FoodDatabase) {
    val scope = rememberCoroutineScope()
    val foodDao = db.foodDao()
    var food by remember { mutableStateOf<Food?>(null) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    LaunchedEffect(foodId) {
        foodDao.getAllFoods().collect { foods ->
            food = foods.find { it.id == foodId }
            food?.let {
                name = it.name
                price = it.price.toString()
                unit = it.unit
                imageUrl = it.imageUrl
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Chỉnh sửa món ăn") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tên món") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Giá") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = unit,
                onValueChange = { unit = it },
                label = { Text("Đơn vị") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL hình ảnh") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    food?.let {
                        val updatedFood = it.copy(
                            name = name,
                            price = price.toIntOrNull() ?: 0,
                            unit = unit,
                            imageUrl = imageUrl
                        )
                        scope.launch { foodDao.updateFood(updatedFood) }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Lưu")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFoodEditScreen() {
    _22a1001d0098_TranTruongGiangTheme {
        val db = FoodDatabase.getDatabase(androidx.compose.ui.platform.LocalContext.current)
        FoodEditScreen(rememberNavController(), 1, db)
    }
}