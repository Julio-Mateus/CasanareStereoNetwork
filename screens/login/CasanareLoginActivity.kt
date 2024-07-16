package com.jcmateus.casanarestereo.screens.login

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.screens.home.CasanareHomeActivity
import com.jcmateus.casanarestereo.ui.theme.CasanareStereoTheme

class CasanareLoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CasanareStereoTheme {
                CasanareLoginScreen(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun CasanareLoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

) {
    //True = Login; False = Create
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
    val token = "792234410149-82bpdkviurrvrr69g49irmemrafdam82.apps.googleusercontent.com"
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try{
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithGoogleCredential(credential) {
                navController.navigate("CasanareHomeActivity.route")
            }
        }
        catch(ex: Exception){
                Log.d("Casanare", "LoginScreen: ${ex.message}")
        }

    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box {
            // Imagen de fondo
            Image(
                painter = painterResource(id = R.drawable.fondo), // Reemplaza con tu imagen
                contentDescription = "Imagen de fondo",
                modifier = Modifier
                    .width(411.dp) // Establece el ancho de la imagen.dp)
                    .height(400.dp),
                contentScale = ContentScale.Crop,
                //colorFilter = ColorFilter.tint(Color(0x07000000), blendMode = BlendMode.Darken) // Color negro con opacidad
            )
            // Capa de oscurecimiento
            Box(
                modifier = Modifier
                    .fillMaxSize()
                //.background(Color(0xFF000000).copy(alpha = 0.5f))
            ) // Color negro con 70% de opacidad
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box() {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo1), // Reemplaza con tu imagen
                        contentDescription = "Logo",
                        modifier = Modifier
                            //.width(48.dp) // Agrega un ancho fijo al logo
                            .size(80.dp) // Establece el tamaño del logo

                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "CASANARE STEREO NETWORK",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 25.sp,
                        color = MaterialTheme.colorScheme.inverseOnSurface // Cambia el color del texto para que se vea sobre la imagen
                    )
                    Text(
                        text = "DONDE LATE EL CORAZÓN DEL LLANO",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.inverseOnSurface  // Cambia el color del texto para que se vea sobre la imagen
                    )
                }
            }
            Spacer(modifier = Modifier.padding(65.dp))
            if (showLoginForm.value) {
                Text(
                    text = "Crea una cuenta",
                    color = MaterialTheme.colorScheme.scrim,
                )
                UserForm(
                    isCreateAccount = true
                ) { email, password ->
                    Log.d("Casanare", "Creando cuenta con$email y $password")
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate("home")
                    }
                }
            } else {
                Text(text = "Inicia sesión", color = MaterialTheme.colorScheme.scrim)
                UserForm(
                    isCreateAccount = false,

                    )
                { email, password ->
                    Log.d("Casanare", "Iniciando sesión con $email y $password")
                    viewModel.signInWithEmailAndPassword(email, password) {
                        navController.navigate("home")
                    }
                }
            }
            Spacer(modifier = Modifier.padding(15.dp))
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val text1 = if (showLoginForm.value) "Ya tienes cuenta?" else "No tienes cuenta?"
                val text2 = if (showLoginForm.value) "Inicia sesión" else "Registrate"
                Text(
                    text = text1, color = MaterialTheme.colorScheme.scrim
                )
                Text(text = text2,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 10.dp),
                    color = MaterialTheme.colorScheme.error

                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        val opciones = GoogleSignInOptions.Builder(
                            GoogleSignInOptions.DEFAULT_SIGN_IN
                        )
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                        val googleSignInClient = GoogleSignIn.getClient(context, opciones)
                        launcher.launch(googleSignInClient.signInIntent)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.logo_de_google_48),
                    contentDescription = "Google",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(10.dp)
                )
                Text(text = "Continuar con Google",
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    )
            }
        }


    }
}

@Composable
fun UserForm(
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val valido = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        EmailInput(
            emailState = email,
            labelId = "Email",

            )
        PasswordInput(
            passwordState = password,
            labelId = "Password",
            passwordVisible = passwordVisible,
        )
        SubmitButton(
            textId = if (isCreateAccount) "Crear cuenta" else "Iniciar sesión",
            inputValido = valido,


            ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }

    }

}

@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClic: () -> Unit
) {
    Button(
        onClick = onClic,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        enabled = inputValido,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF994545),
        )
    ) {
        Text(
            text = textId,
            modifier = Modifier
                .padding(5.dp),
            MaterialTheme.colorScheme.scrim

        )
    }

}

@Composable
// Campo de contraseña
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId, color = MaterialTheme.colorScheme.scrim) },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        //MaterialTheme.colorScheme.inverseOnSurface,
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            } else null
        }
    )

}

@Composable
//Visibilidad del icono de la contraseña
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image =
        if (passwordVisible.value)
            Icons.Default.VisibilityOff
        else
            Icons.Default.Visibility
    IconButton(onClick = {
        passwordVisible.value = !passwordVisible.value
    }) {
        Icon(
            imageVector = image,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email"
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email,
    )

}

@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId, color = MaterialTheme.colorScheme.scrim) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CasanareStereoTheme {
        CasanareLoginScreen(navController = rememberNavController())
    }
}





