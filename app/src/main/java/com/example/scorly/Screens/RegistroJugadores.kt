package com.example.scorly.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scorly.Models.Jugador
import kotlinx.coroutines.launch

@Composable
fun RegistroJugadorScreen(
    equipos: List<String>,
    jugadorExistente: Jugador? = null,
    numerosUsadosPorEquipo: (String) -> List<Int>,
    onGuardar: (Jugador) -> Unit,
    onModificar: (Jugador) -> Unit,
    onBaja: (Jugador) -> Unit,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scroll = rememberScrollState()

    // CAMPOS
    var nombre by remember { mutableStateOf(jugadorExistente?.nombre ?: "") }
    var apellido by remember { mutableStateOf(jugadorExistente?.apellido ?: "") }
    var nacionalidad by remember { mutableStateOf(jugadorExistente?.nacionalidad ?: "") }
    var fotoUrl by remember { mutableStateOf(jugadorExistente?.foto_url ?: "") }
    var fechaNacimiento by remember { mutableStateOf(jugadorExistente?.fecha_nacimiento ?: "") }
    var posicion by remember { mutableStateOf(jugadorExistente?.posicion ?: "") }
    var equipo by remember { mutableStateOf(jugadorExistente?.equipo_id?.toString() ?: "") }
    var numero by remember { mutableStateOf(jugadorExistente?.numero_camiseta?.toString() ?: "") }

    // ERRORES
    var errorCampos by remember { mutableStateOf(false) }
    var errorNumero by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFF141414), Color(0xFF2323FF), Color(0xFF141414)))
        ))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(scroll)
                .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                .border(1.dp, Color.White, RoundedCornerShape(20.dp))
                .padding(22.dp)
        ) {

            Text("REGISTRO DE JUGADOR", color = Color.White, fontWeight = FontWeight.Bold,
                fontSize = 26.sp, modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)

            Spacer(Modifier.height(20.dp))

            CampoTexto("Nombre", nombre) { nombre = it }
            CampoTexto("Apellido", apellido) { apellido = it }
            CampoTexto("Nacionalidad", nacionalidad) { nacionalidad = it }
            CampoTexto("Foto URL (opcional)", fotoUrl) { fotoUrl = it }
            CampoTexto("Fecha de nacimiento (AAAA-MM-DD)", fechaNacimiento) { fechaNacimiento = it }

            DropDownOpciones("Posición", posicion, listOf("PORTERO","DEFENSA","MEDIO","DELANTERO")) { posicion = it }
            CampoTexto("Equipo ID", equipo, isNumber = true) { equipo = it }
            CampoTexto("Número de camiseta", numero, isNumber = true) { numero = it }

            if (errorCampos) Text("⚠ Falta llenar campos", color = Color.Yellow)
            if (errorNumero) Text("⚠ Número ya usado en este equipo", color = Color.Yellow)

            Spacer(Modifier.height(26.dp))

            fun validar(): Boolean {
                if (nombre.isBlank() || apellido.isBlank() || posicion.isBlank() || equipo.isBlank() || numero.isBlank()) {
                    errorCampos = true; return false
                }
                val nEquipo = equipo.toIntOrNull() ?: return false
                val nNumero = numero.toIntOrNull() ?: return false
                if (numerosUsadosPorEquipo(nEquipo.toString()).contains(nNumero)) {
                    errorNumero = true; return false
                }
                return true
            }

            BotonCool("GUARDAR") {
                if (validar()) scope.launch {
                    onGuardar(
                        Jugador(
                            jugador_id = 0,
                            equipo_id = equipo.toInt(),
                            nombre = nombre,
                            apellido = apellido,
                            posicion = posicion,
                            numero_camiseta = numero.toInt(),
                            nacionalidad = nacionalidad,
                            foto_url = fotoUrl.ifBlank { null },
                            fecha_nacimiento = fechaNacimiento.ifBlank { null }
                        )
                    )
                }
            }

            BotonCool("MODIFICAR") {
                jugadorExistente?.let {
                    if (validar()) onModificar(
                        it.copy(
                            nombre = nombre,
                            apellido = apellido,
                            posicion = posicion,
                            equipo_id = equipo.toInt(),
                            numero_camiseta = numero.toInt(),
                            nacionalidad = nacionalidad,
                            foto_url = fotoUrl.ifBlank { null },
                            fecha_nacimiento = fechaNacimiento.ifBlank { null }
                        )
                    )
                }
            }

            BotonCool("BAJA") { jugadorExistente?.let { onBaja(it) } }
            BotonCool("REGRESAR") { onBack() }
        }
    }
}

@Composable
fun CampoTexto(label:String, value:String, isNumber:Boolean=false, onChange:(String)->Unit){
    OutlinedTextField(
        value = value, onValueChange = onChange,
        label = { Text(label, color = Color.White) },
        keyboardOptions = KeyboardOptions(keyboardType = if (isNumber) KeyboardType.Number else KeyboardType.Text),
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        textStyle = LocalTextStyle.current.copy(color = Color.White)
    )
}

@Composable
fun DropDownOpciones(label:String, value:String, opciones:List<String>, onSelect:(String)->Unit){
    var expand by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value, onValueChange = {}, readOnly = true,
            label = { Text(label, color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            trailingIcon = {
                IconButton(onClick={ expand=true }) { Icon(Icons.Filled.ArrowDropDown, "") }
            }
        )
        DropdownMenu(expanded = expand, onDismissRequest = { expand=false }) {
            opciones.forEach {
                DropdownMenuItem(text={ Text(it) }, onClick={ expand=false; onSelect(it) })
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun BotonCool(text:String, onClick:()->Unit){
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
