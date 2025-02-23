@file:Suppress("DEPRECATION")

package com.jcmateus.casanarestereo.screens.login

//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.width
//import androidx.compose.ui.graphics.BlendMode
//import androidx.compose.ui.graphics.ColorFilter
//import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn.getClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.jcmateus.casanarestereo.HomeApplication
import com.jcmateus.casanarestereo.R
import com.jcmateus.casanarestereo.screens.formulario.PantallaFormulario
import com.jcmateus.casanarestereo.screens.home.Destinos
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraRepository
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraViewModel
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.EmisoraViewModelFactory
import com.jcmateus.casanarestereo.screens.usuarios.emisoras.PerfilEmisora
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.comparisons.then


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CasanareLoginScreen(
    navController: NavHostController,
    emisoraViewModel: EmisoraViewModel,
    loginViewModel: LoginScreenViewModel
) {
    Log.d("CasanareLoginScreen", "CasanareLoginScreen: Iniciando CasanareLoginScreen")
    val application = LocalContext.current.applicationContext as HomeApplication
    val dataStoreManager = application.dataStoreManager
    val viewModel = loginViewModel
    var isUsuarioChecked by remember { mutableStateOf(false) }
    var isEmisoraChecked by remember { mutableStateOf(false) }

    var isUsuarioCheckedBeforeEmisora by remember { mutableStateOf(false) }
    var selectedRol by remember { mutableStateOf<Rol?>(null) } // Rol seleccionado
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(initialValue = false)
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle(initialValue = null)
    var checkTerminos by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() } // Crear SnackbarHostState
    // val authState by viewModel.authState.collectAsState()
    val successMessage by viewModel.successMessage.collectAsStateWithLifecycle(initialValue = null)
    //True = Login; False = Create
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
    val scope = rememberCoroutineScope()
    val showDialogFlow: Flow<Boolean> = dataStoreManager.getShowDialog()
    var showTermsDialog by remember { mutableStateOf(false) }
    var showTermsDialogCreateAccount by remember { mutableStateOf(false) } // Para términos y condiciones de crear cuenta
    var googleSignInRequested by remember { mutableStateOf(false) }
    val token = "792234410149-33q9p90prlr9migq7ssdmjanlklc7285.apps.googleusercontent.com"
    val context = LocalContext.current
    var rolUsuario by remember { mutableStateOf<Rol?>(null) }
    LaunchedEffect(key1 = Unit) {
        rolUsuario = dataStoreManager.getRol()
            .first()
    }
    val authState = viewModel.authState.collectAsStateWithLifecycle()
    //val currentAuthState by authState.collectAsState() // Convertir a State
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        Log.d("CasanareLoginScreen", "launcher: Callback del launcher ejecutado")
        Log.d("CasanareLoginScreen", "launcher: resultCode = ${result.resultCode}")
        Log.d("CasanareLoginScreen", "launcher: result.data = ${result.data}")
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("CasanareLoginScreen", "launcher: Resultado OK")
            val data: Intent? = result.data
            if (data != null) {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account = task.getResult(ApiException::class.java)

                    if (account != null) {
                        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                        if (selectedRol != null) {
                            viewModel.iniciarSesionConGoogle(context, credential, selectedRol)
                            // La navegación y el manejo del éxito se hacen en observeAuthState
                            Log.d(
                                "CasanareLoginScreen",
                                "launcher: Se llamo a iniciarSesionConGoogle"
                            )
                        } else {
                            Log.e("CasanareLoginScreen", "launcher: selectedRol es null")
                            viewModel.viewModelScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Por favor, selecciona un rol.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    } else {
                        Log.e("CasanareLoginScreen", "launcher: account es null")
                        viewModel.viewModelScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Error al obtener la cuenta de Google.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("CasanareLoginScreen", "Google sign in failed", e)
                    viewModel.viewModelScope.launch { // Usar viewModelScope.launch
                        snackbarHostState.showSnackbar(
                            message = "Error al iniciar sesion con google",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            } else {
                Log.e("CasanareLoginScreen", "launcher: data es null")
            }
        } else {
            Log.e("CasanareLoginScreen", "launcher: Resultado no OK")
        }
    }
    val navigateTo by loginViewModel.navigateTo.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = navigateTo) {
        if (navigateTo != null) {
            navController.navigate(navigateTo!!)
            loginViewModel.clearSuccessMessage() // Limpiar el mensaje y la ruta después de navegar
        }
    }

    // Lógica para asegurar que solo se pueda seleccionar un rol a la vez
    LaunchedEffect(key1 = isUsuarioChecked, key2 = isEmisoraChecked) {
        if (isUsuarioChecked && isEmisoraChecked) {
            // Si ambos están seleccionados, deseleccionar el que se seleccionó primero
            if (isUsuarioChecked) {
                isEmisoraChecked = false
            } else {
                isUsuarioChecked = false
            }
        }
        // Actualizar selectedRol
        selectedRol =
            if (isUsuarioChecked) Rol.USUARIO else if (isEmisoraChecked) Rol.EMISORA else null
    }

    val termsAcceptedFlow = application.dataStoreManager.getTermsAccepted()
        .collectAsStateWithLifecycle(initialValue = false)

    if (!termsAcceptedFlow.value) {
        // Mostrar el checkbox de términos y condiciones
    }

    var showCheckbox by remember { mutableStateOf(!termsAcceptedFlow.value) } // Mostrar checkbox si no se han aceptado los términos

    // Mostrar indicador de progreso si isLoading es true
    if (isLoading) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
    when {
        // Mostrar mensaje de error si errorMessage no es nulo
        errorMessage != null -> {
            ShowSnackbar(snackbarHostState, errorMessage!!) {
                viewModel.clearErrorMessage() // Limpiar el mensaje de error después de mostrarlo
            }
        }
        // Mostrar mensaje de éxito si successMessage no es nulo
        successMessage != null -> {
            ShowSnackbar(snackbarHostState, successMessage!!) {
                viewModel.clearSuccessMessage() // Limpiar el mensaje de éxito después de mostrarlo
            }
        }
    }
    val coroutineScope = rememberCoroutineScope() // Obtener CoroutineScope
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionsMap ->
            val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
            if (areGranted) {
                // Permisos concedidos
                coroutineScope.launch { // Lanzar una corrutina
                    dataStoreManager.setLocationPermissionGranted(true)
                }
            } else {
                // Permisos denegados
            }
        }
    )
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) } // Mover SnackbarHost aquí
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Imagen de fondo
                Image(
                    painter = painterResource(id = R.drawable.fondo), // Reemplaza con tu imagen
                    contentDescription = "Imagen de fondo",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF000000).copy(alpha = 0.9f)),
                    contentScale = ContentScale.Crop,
                )
                // Capa de oscurecimiento
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF000000).copy(alpha = 0.7f))
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                Box {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo1), // Reemplaza con tu imagen
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(120.dp) // Establece el tamaño del logo
                        )
                        Spacer(modifier = Modifier.padding(20.dp))
                        Text(
                            text = "CASANARE STEREO NETWORK",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.surfaceBright // Cambia el color del texto para que se vea sobre la imagen
                        )
                        Text(
                            text = "DONDE LATE EL CORAZÓN DEL LLANO",
                            style = MaterialTheme.typography.bodySmall,
                            //fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.surfaceBright  // Cambia el color del texto para que se vea sobre la imagen
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(15.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.surfaceBright)
                        .clickable {
                            Log.d("CasanareLoginScreen", "Button: Botón de Google presionado")
                            if (showLoginForm.value) {
                                // Si es creación de cuenta, verificar términos y rol
                                if (!checkTerminos) {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Debes aceptar los términos y condiciones",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                } else if (selectedRol == null) {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Debes seleccionar un rol",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                } else {
                                    // Si todo está bien, iniciar el proceso de Google Sign-In
                                    val opciones = GoogleSignInOptions
                                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(token)
                                        .requestEmail()
                                        .build()
                                    val googleSignInClient = getClient(context, opciones)
                                    launcher.launch(googleSignInClient.signInIntent)
                                }
                            } else {
                                // Si es inicio de sesión, iniciar el proceso de Google Sign-In directamente
                                val opciones = GoogleSignInOptions
                                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(token)
                                    .requestEmail()
                                    .build()
                                val googleSignInClient = getClient(context, opciones)
                                launcher.launch(googleSignInClient.signInIntent)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_de_google_48),
                        contentDescription = "Google",
                        modifier = Modifier
                            .size(38.dp)
                            .padding(6.dp)
                    )
                    Text(
                        text = "Continuar con Google",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.scrim
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // ----> Lógica de selección de rol <----
                if (showLoginForm.value) { // Mostrar solo si es creación de cuenta
                    Row {
                        Checkbox(
                            checked = isUsuarioChecked,
                            onCheckedChange = {
                                isUsuarioChecked = it
                                isEmisoraChecked = false
                                selectedRol = if (it) Rol.USUARIO else null
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedColor = MaterialTheme.colorScheme.surfaceBright
                            )
                        )
                        Text("Usuario", color = MaterialTheme.colorScheme.surfaceBright)

                        Checkbox(
                            checked = isEmisoraChecked,
                            onCheckedChange = {
                                isEmisoraChecked = it
                                isUsuarioChecked = false
                                selectedRol = if (it) Rol.EMISORA else null
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedColor = MaterialTheme.colorScheme.surfaceBright
                            )
                        )
                        Text("Emisora", color = MaterialTheme.colorScheme.surfaceBright)
                    }
                }
                if (showLoginForm.value) {
                    Text(
                        text = "Crea una cuenta",
                        color = MaterialTheme.colorScheme.surfaceBright,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    UserForm(
                        isCreateAccount = true,
                        viewModel = viewModel,
                        selectedRol = selectedRol,
                        snackbarHostState = snackbarHostState,
                        coroutineScope = scope,
                        checkTerminos = checkTerminos,
                        onCheckTerminosChange = { checkTerminos = it },
                        showTermsDialog = showTermsDialog,
                        onShowTermsDialogChange = { showTermsDialog = it }
                    )
                } else {
                    Text(
                        text = "Inicia sesión",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.surfaceBright
                    )
                    UserForm(
                        isCreateAccount = false,
                        viewModel = viewModel,
                        selectedRol = selectedRol,
                        snackbarHostState = snackbarHostState,
                        coroutineScope = scope,
                        checkTerminos = checkTerminos,
                        onCheckTerminosChange = { checkTerminos = it },
                        showTermsDialog = showTermsDialog,
                        onShowTermsDialogChange = { showTermsDialog = it }
                    )
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Row(
                modifier = Modifier.padding(top = 700.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val text1 = if (
                    showLoginForm.value) "Ya tienes cuenta?" else "No tienes cuenta?"
                val text2 = if (showLoginForm.value) "Inicia sesión" else "Registrate"
                Text(
                    text = text1, color = MaterialTheme.colorScheme.surfaceBright,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                )
                Text(text = text2,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 15.dp),
                    color = MaterialTheme.colorScheme.error

                )
            }
        }
        // Mostrar SnackbarHost
        SnackbarHost(hostState = snackbarHostState)
    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun UserForm(
    isCreateAccount: Boolean = false,
    viewModel: LoginScreenViewModel,
    selectedRol: Rol?,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    checkTerminos: Boolean,
    onCheckTerminosChange: (Boolean) -> Unit,
    showTermsDialog: Boolean,
    onShowTermsDialogChange: (Boolean) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val valido = if (isCreateAccount) {
        email.trim().isNotEmpty() && password.trim()
            .isNotEmpty() && checkTerminos && selectedRol != null
    } else {
        email.trim().isNotEmpty() && password.trim().isNotEmpty()
    }
    val buttonEnabled = if (isCreateAccount) {
        valido && selectedRol != null && checkTerminos
    } else {
        valido
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentHeight()
    ) {
        EmailInput(
            emailState = remember { mutableStateOf(email) },
            labelId = "Correo",
            onValueChange = { email = it },
            imeAction = if (isCreateAccount) ImeAction.Next else ImeAction.Done
        )
        PasswordInput(
            passwordState = remember { mutableStateOf(password) },
            labelId = "Contraseña",
            passwordVisible = remember { mutableStateOf(passwordVisible) },
            onValueChange = { password = it },
            imeAction = if (isCreateAccount) ImeAction.Done else ImeAction.Done
        )

        if (isCreateAccount) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checkTerminos,
                    onCheckedChange = {
                        onCheckTerminosChange(it)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedColor = MaterialTheme.colorScheme.surfaceBright
                    )
                )
                Text(
                    text = "Acepto los términos y condiciones",
                    color = MaterialTheme.colorScheme.surfaceBright,
                    modifier = Modifier.clickable {
                        onShowTermsDialogChange(true)
                    }
                )
            }
            if (showTermsDialog) {
                AlertDialog(
                    onDismissRequest = { onShowTermsDialogChange(false) },
                    title = { Text("Términos y Condiciones") },
                    text = {
                        Text(
                            "Aquí van los términos y condiciones. " +
                                    "Puedes agregar un texto largo o incluso cargar los términos desde un archivo."
                        )
                    },
                    confirmButton = {
                        Button(onClick = { onShowTermsDialogChange(false) }) {
                            Text("Aceptar")
                        }
                    }
                )
            }
        }

        Button(
            onClick = {
                if (isCreateAccount) {
                    if (!checkTerminos) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Debes aceptar los términos y condiciones",
                                duration = SnackbarDuration.Short
                            )
                        }
                        return@Button
                    }
                    if (selectedRol == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Debes seleccionar un rol",
                                duration = SnackbarDuration.Short
                            )
                        }
                        return@Button
                    }
                    // Si llegamos aquí, selectedRol no es null
                    selectedRol?.let {
                        viewModel.crearUsuarioConCorreoYContrasena(
                            email.trim(),
                            password.trim(),
                            checkTerminos,
                            it // Usamos it (que es el Rol no nulo)
                        )
                    }
                } else {
                    if (valido) {
                        viewModel.iniciarSesionConCorreoYContrasena(
                            email.trim(),
                            password.trim()
                        )
                        keyboardController?.hide()
                    }
                }
            },
            enabled = buttonEnabled,
            modifier = Modifier
                .padding(14.dp)
                .height(54.dp)
                .fillMaxWidth()
                .then(
                    if (valido) Modifier.shadow(
                        elevation = 2.dp,
                        shape = MaterialTheme.shapes.medium
                    ) else Modifier
                )
        ) {
            Text(
                text = if (isCreateAccount) "Crear cuenta" else "Iniciar sesión",
                color = MaterialTheme.colorScheme.surfaceBright,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun ShowFloatingMessage(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarHost(hostState = snackbarHostState)

    LaunchedEffect(key1 = message) {
        snackbarHostState.showSnackbar(
            message = message,
            duration = duration
        )
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShowSnackbar(snackbarHostState: SnackbarHostState, message: String, action: () -> Unit) {
    LaunchedEffect(key1 = message) { // Lanzar la corrutina con LaunchedEffect
        val snackbarResult = snackbarHostState.showSnackbar(
            message = message,
            withDismissAction = true,
            duration = SnackbarDuration.Short,
            actionLabel = "Aceptar"
        )
        if (snackbarResult == SnackbarResult.ActionPerformed || snackbarResult == SnackbarResult.Dismissed) {
            action()
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
            .padding(16.dp)
            .height(64.dp)
            .fillMaxWidth()
            .then(
                if (inputValido) Modifier.shadow(
                    elevation = 2.dp,
                    shape = MaterialTheme.shapes.medium
                ) else Modifier
            ), // Añadir sombra si está habilitado,
        shape = MaterialTheme.shapes.medium,
        enabled = inputValido,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (inputValido) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surfaceVariant, // Cambiar color si está deshabilitado
            contentColor = if (inputValido) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant // Cambiar color si está deshabilitado
        )

    ) {
        Text(
            text = textId,
            modifier = Modifier
                .padding(8.dp),
            color = if (inputValido) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp, // Cambiar color si está deshabilitado
            // ... otros estilos

        )
    }

}

@Composable
// Campo de contraseña
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Done // Añadido: Acción del teclado

) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it
            onValueChange(it)
        },
        label = { Text(text = labelId, color = MaterialTheme.colorScheme.onPrimary) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            // Usamos los parámetros correctos de OutlinedTextFieldDefaults.colors()
            focusedTextColor = MaterialTheme.colorScheme.surfaceBright, // Color del texto cuando está enfocado
            unfocusedTextColor = MaterialTheme.colorScheme.surfaceBright, // Color del texto cuando no está enfocado
            focusedBorderColor = MaterialTheme.colorScheme.primary, // Color del borde cuando está enfocado
            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary, // Color del borde cuando no está enfocado
            cursorColor = MaterialTheme.colorScheme.onPrimary, // Color del cursor
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction // Añadido: Acción del teclado
        ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            }
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.surfaceBright, // Color del texto
            fontSize = 18.sp // Tamaño de la fuente
        )
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
            tint = MaterialTheme.colorScheme.surfaceBright
        )
    }
}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email",
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onValueChange = onValueChange

    )

}

@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType,
    imeAction: ImeAction = ImeAction.Next, // Añadido: Acción del teclado
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {
            valueState.value = it
            onValueChange(it)
        },
        label = { Text(text = labelId, color = MaterialTheme.colorScheme.onPrimary) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            // Usamos los parámetros correctos de OutlinedTextFieldDefaults.colors()
            focusedTextColor = MaterialTheme.colorScheme.surfaceBright, // Color del texto cuando está enfocado
            unfocusedTextColor = MaterialTheme.colorScheme.surfaceBright, // Color del texto cuando no está enfocado
            focusedBorderColor = MaterialTheme.colorScheme.primary, // Color del borde cuando está enfocado
            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary, // Color del borde cuando no está enfocado
            cursorColor = MaterialTheme.colorScheme.onPrimary, // Color del cursor

        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction // Añadido: Acción del teclado
        ),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.surfaceBright, // Color del texto
            fontSize = 18.sp // Tamaño de la fuente
        )
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PreviewContent() {
    //val context = LocalContext.current
    //val application = context.applicationContext as HomeApplication // Obtener HomeApplication
    val navController = rememberNavController() // Usar rememberNavController()
    //val emisoraViewModel = application.emisoraViewModel // Obtener emisoraViewModel de HomeApplication
    val emisoraViewModel: EmisoraViewModel = viewModel()
    val loginViewModel: LoginScreenViewModel = viewModel()

    CasanareLoginScreen(
        navController = navController,
        emisoraViewModel = emisoraViewModel,
        loginViewModel = loginViewModel
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PreviewContent()
}





