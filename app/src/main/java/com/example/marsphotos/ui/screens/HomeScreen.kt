/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.ui.screens

import PicsumUiState

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.marsphotos.R
import com.example.marsphotos.network.MarsPhoto
import com.example.marsphotos.network.PicsumPhoto


@Composable
fun HomeScreen(
    marsUiState: MarsUiState, modifier: Modifier = Modifier,
    picsumUiState: PicsumUiState
) {
    if(marsUiState is MarsUiState.Error || picsumUiState is PicsumUiState.Error){
        ErrorScreen(modifier=modifier.fillMaxSize())
    }else if(marsUiState is MarsUiState.Success && picsumUiState is PicsumUiState.Success){
        ResultScreen3(
            marsphotos = marsUiState.photos,
            randomMarsPhoto = marsUiState.randomPhoto,
            listMarsPhotos = marsUiState.listphotos,
            picsumphotos =picsumUiState.photos ,
            randomPicsumPhoto = picsumUiState.randomPhoto,
            listPicsumPhotos = picsumUiState.listphotos,
        )
    }
    else{
        LoadingScreen(modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultScreen(photos: String, randomPhoto: MarsPhoto, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()){
        Text(text = photos)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(randomPhoto.imgSrc) .crossfade(true) .build(),
            contentDescription = "A photo",
        )
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultScreen2(photos: String, randomPhoto: PicsumPhoto, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()){
        Text(text = photos)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(randomPhoto.imgSrc) .crossfade(true) .build(),
            contentDescription = "A photo",
        )
    }
}

@Composable
fun ResultScreen3(marsphotos: String,
                 randomMarsPhoto : MarsPhoto,
                  listMarsPhotos: List<MarsPhoto>,
                 picsumphotos:String,
                 randomPicsumPhoto: PicsumPhoto,
                  listPicsumPhotos : List <PicsumPhoto>,
                 modifier: Modifier = Modifier) {
    var currentRandomMarsPhoto by remember { mutableStateOf(randomMarsPhoto) }
    var currentRandomPicsumPhoto by remember { mutableStateOf(randomPicsumPhoto) }

    Column( modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = picsumphotos)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentRandomPicsumPhoto.imgSrc)
                .crossfade(true)
                .build(),
            contentDescription = "A photo",
        )
        Text(text = marsphotos)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentRandomMarsPhoto.imgSrc)
                .crossfade(true)
                .build(),
            contentDescription = "A photo",

        )



        Button(onClick = {
            currentRandomPicsumPhoto = listPicsumPhotos.random()
            currentRandomMarsPhoto = listMarsPhotos.random()
        }) {
            Text("Roll")
        }
    }
}





