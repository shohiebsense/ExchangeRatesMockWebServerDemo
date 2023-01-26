package com.shohiebsense.conversionapp.ui.page.home

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.shohiebsense.conversionapp.ConversionApp


val textFieldModifier = Modifier.fillMaxWidth()
fun ColumnScope.rowModifier() = Modifier.fillMaxWidth()
val gridModifier = Modifier.fillMaxWidth()
val itemModifier = Modifier.fillMaxWidth()


@Composable
fun HomePage() {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val currentRequest = homeViewModel.convertRequest
    val context = LocalContext.current
    val isDialogShowing = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = currentRequest.value.value.toString(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = {
                val newValue = it.toIntOrNull() ?: 0
                currentRequest.value.value = newValue
            }, modifier = textFieldModifier
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier
                .align(Alignment.End)
                .background(Color.LightGray)
                .padding(16.dp)
                .border(1.dp, Color.Gray)
                .clickable {
                    isDialogShowing.value = true
                }, horizontalArrangement = Arrangement.End
        ) {
            Text(text = currentRequest.from.value)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            modifier = gridModifier,
            columns = GridCells.Fixed(3),
            content = {
                items(homeViewModel.currencyAndValueList) {
                    ConversionItem(it)
                }
            })
    }
    CurrencyChoicesDialog(
        isShowing = isDialogShowing,
        homeViewModel.currencyList
    ) {
        currentRequest.from.value = it
        homeViewModel.convert()
    }
    LaunchedEffect(homeViewModel.isInitialized) {
        if (!homeViewModel.isInitialized) {
            homeViewModel.init(
                context.getSharedPreferences(
                    ConversionApp.APP_SHARED_PREFERENCES,
                    Context.MODE_PRIVATE
                )
            ) {
                //error toast message or anything
            }
            homeViewModel.isInitialized = true
        }
    }
}

@Composable
private fun CurrencyChoicesDialog(
    isShowing: MutableState<Boolean>,
    currencyList: List<String>,
    onButtonClicked: (String) -> Unit
) {
    if (isShowing.value) {
        Dialog(onDismissRequest = { isShowing.value = false }) {
            Surface(
                modifier = Modifier.padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                ) {
                    items(currencyList) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isShowing.value = false
                                onButtonClicked(it)
                            }) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = it)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                    }
                }
            }
        }
    }
}

@Composable
private fun ConversionItem(
    currencyAndValuePair: Pair<String, Double>
) {
    Column(
        modifier = Modifier
            .size(48.dp)
            .border(width = 1.dp, color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = currencyAndValuePair.second.toString(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = currencyAndValuePair.first,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        )
    }
}