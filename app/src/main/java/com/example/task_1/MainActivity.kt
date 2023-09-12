package com.example.task_1

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task_1.ui.theme.TASK_1Theme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TASK_1Theme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Struct()
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Struct() {
    val context = LocalContext.current
    val localDensity = LocalDensity.current
    var HeightDp by remember { mutableStateOf(0.dp) }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    HeightDp = with(localDensity) { coordinates.size.height.toDp() }},
                title = {
                    Text(
                        text = context.getString(R.string.app_name),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Magenta)
            )
        },
        content = { padding -> Quiz(Modifier.padding(padding), HeightDp) },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Quiz(modifier: Modifier, Topbar: Dp) {
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    var listStat by remember { mutableStateOf(generateStatements()) }
    Column (
        modifier = Modifier.padding(top =  Topbar + 5.dp)
    ){
        Row {
            Text(text = context.getString(R.string.quiz_instr_header),
                 modifier = Modifier.padding(10.dp),
                 fontSize = 30.sp,
                fontWeight = FontWeight.Black
            )
        }
        Row {
            Text(text = context.getString(R.string.quiz_instr_description),
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(10.dp))
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            OutlinedTextField(value = text,
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Magenta,
                    unfocusedBorderColor = Color.Magenta,
                    unfocusedSupportingTextColor = Color.Magenta,
                    cursorColor = Color.Magenta),
                onValueChange = {
                text = it },
            )
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
        ){
            Button(
                modifier = Modifier.padding(10.dp).height(50.dp).width(110.dp),
                colors = ButtonDefaults.buttonColors(Color.Black),
                border = BorderStroke(width = 3.dp, color = Color.Magenta),
                onClick = {
                    if (text != "" && text.length > 4){
                        listStat = listStat + Statement(text, true)
                        text = ""
                    }
                }) {
                Text(text = context.getString(R.string.button_true), color = Color.Magenta)
            }
            Button(
                modifier = Modifier.padding(10.dp).height(50.dp).width(110.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                border = BorderStroke(width = 3.dp, color = Color.Magenta),
                onClick = {
                    if (text != "" && text.length > 4){
                        listStat = listStat + Statement(text, false)
                        text = ""
                    }
                }) {
                Text(text = context.getString(R.string.button_false), color = Color.Magenta)
            }
        }
        Row (

        ){
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                userScrollEnabled = true
            ){
                items(listStat){currentSta ->
                    Divider(color = Color.Magenta, thickness = 2.dp)
                    Text(
                        text = currentSta.statement,
                        modifier = Modifier.padding(10.dp).combinedClickable(
                            onClick = {
                                if (currentSta.isTrue){
                                    listStat = listStat - currentSta
                                    Toast.makeText(context, context.getString(R.string.answer_is_true),
                                        Toast.LENGTH_SHORT).show()
                                    if (listStat.size == 0){
                                        Toast.makeText(context, context.getString(R.string.Won),
                                            Toast.LENGTH_SHORT).show()
                                    }

                                }
                                else{
                                    Toast.makeText(context, context.getString(R.string.wrong_answer),
                                        Toast.LENGTH_SHORT).show()
                                }
                            },
                            onLongClick = {
                                if (currentSta.isTrue){
                                    Toast.makeText(context, context.getString(R.string.wrong_answer),
                                        Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    listStat = listStat - currentSta
                                    Toast.makeText(context, context.getString(R.string.answer_is_false),
                                        Toast.LENGTH_SHORT).show()
                                    if (listStat.size == 0){
                                        Toast.makeText(context, context.getString(R.string.Won),
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                        ),
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}

//Dta class que genera el tipo "statement"
data class Statement(
    val statement: String,
    val isTrue: Boolean,
    val statementID: UUID = UUID.randomUUID()
)

//Funcion para generar las preguntas del quiz
private fun generateStatements(): List<Statement> {
    return arrayListOf(
        Statement("A \'val\' and \'var\' are the same.", false),
        Statement("Mobile Application Development grants 12 ECTS.", false),
        Statement("A unit in Kotlin corresponds to a void in Java.", true),
        Statement("In Kotlin \'when\' replaces the \'switch\' operator in Java.", true)
    )
}