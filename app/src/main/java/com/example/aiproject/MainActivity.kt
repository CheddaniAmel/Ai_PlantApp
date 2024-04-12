package com.example.aiproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.Icon

import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.aiproject.ui.theme.AiProjectTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val Environments = listOf("Indoor", "Outdoor")
        val Hydration = listOf("Always watering", "Sometimes watering")
        val others = listOf("Dark roots","Wilting", "Yellow leafs" , "Fangus or mold" )

        var door=""
        var water=""
        var leaf=""

        val client=RetrofitClient.instance

        super.onCreate(savedInstanceState)
        setContent {
            AiProjectTheme {
                var issue by remember {
                    mutableStateOf("")
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.bg)
                ) {


                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp)
                    ) {

                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp),
                        ) {
                            Text(
                                text = "Welcome To ",
                                letterSpacing = 0.1.sp,
                                modifier = Modifier.padding(top = 40.dp),
                                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                fontSize = 25.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(0.dp))
                            Text(
                                text = "Plantino ",
                                letterSpacing = 0.1.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                fontSize = 35.sp,
                                color = Color.Black
                            )


                        }

                        Box(
                            modifier = androidx.compose.ui.Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp),
                        ) {
                            homecard()
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(start = 140.dp)

                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.home_deco),
                                    contentDescription = "",
                                    contentScale = ContentScale.Fit,

                                    modifier = Modifier
                                        .size(350.dp)
                                )
                            }
                        }


                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp, start = 20.dp, end = 20.dp)
                        ){

                            Column{
                                dropdown(Environments,"Environment"){
                                    door=it
                                }
                                dropdown(Hydration,"Hydration Frequencies"){
                                    water=it
                                }
                                dropdown(others,"Other symptoms"){
                                    leaf=it
                                }

                                showDisease{
                                    val call=client.getIssue(
                                        Request(door, water, leaf)
                                    )
                                        call.enqueue(object : Callback<Responce>{
                                            override fun onResponse(
                                                call: Call<Responce>,
                                                response: Response<Responce>
                                            ) {
                                                issue="The plant is suffering from ${response.body()?.issue}"
                                            }

                                            override fun onFailure(
                                                call: Call<Responce>,
                                                t: Throwable
                                            ) {
                                                Toast.makeText(
                                                    baseContext,
                                                    t.localizedMessage,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                issue="Check your connection then try again"
                                            }

                                        })
                                }
                                Text(
                                    text = issue,
                                    fontFamily = FontFamily(Font(R.font.roboto_bolditalic)),
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                )


                            }
                        }


                            /*SymptomsList(repository.getSymptomsBlocking())*/

                    }
                }
            }
        }

    }

}


@Composable
fun dropdown(mCities:List<String>,Label:String,selected:(item:String)->Unit){
    var mExpanded by remember { mutableStateOf(false) }
    var mSelectedText by remember { mutableStateOf("") }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(top = 5.dp)) {

        OutlinedTextField(
            value = mSelectedText,
            onValueChange = { mSelectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->

                    mTextFieldSize = coordinates.size.toSize()
                },
            readOnly = true,
            label = {

                Text(
                    text = Label ,
                    fontFamily = FontFamily(Font(R.font.roboto_bolditalic)),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                )

                    },
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded })
            }
        )

        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
        ) {
            mCities.forEachIndexed { index, s -> DropdownMenuItem(
                text = { Text(text = s)},
                onClick = {
                    mSelectedText=mCities[index]
                    selected(when(mSelectedText){
                        "Always watering" -> "Always"
                        "Sometimes watering"->"Sometimes"
                        "Dark roots"->"Darkroot"
                        "Yellow leafs"->"Yellow_leaves"
                        "Fangus or mold"->"Fungus"
                        else -> mSelectedText
                    })
                    mExpanded=false
                }) }
        }
    }
}

@Composable
fun showDisease(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(10.dp)
            .padding(top = 10.dp)
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.run { buttonColors(Color(0xFFB5642D)) }
    ) {
        Text(text = "Show Disease", color = Color.White)
    }
}
@Composable
fun homecard(){
    Box{
        Card(
            modifier = Modifier
                .padding(end = 30.dp, top = 20.dp)
                .fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0, 0, 0, 0))
                    .height(150.dp)
            ) {
                // Element at the bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .size(100.dp),
                ){
                    // Background image
                    Image(
                        painter = painterResource(id = R.drawable.categ_bg),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop,

                        )
                }

                Column (


                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                ){
                    Text(text = "Discover plant   ",
                        fontFamily = FontFamily(Font(R.font.roboto_bolditalic)),
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start =10.dp,top = 30.dp, end = 20.dp))

                    Text(text ="diseases by " ,
                        fontFamily = FontFamily(Font(R.font.roboto_bolditalic)),
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start =10.dp)
                        )

                    Text(text = " examining symptoms",
                        fontFamily = FontFamily(Font(R.font.roboto_bolditalic)),
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start =10.dp)
                        )

                }
            }
        }
    }
}








