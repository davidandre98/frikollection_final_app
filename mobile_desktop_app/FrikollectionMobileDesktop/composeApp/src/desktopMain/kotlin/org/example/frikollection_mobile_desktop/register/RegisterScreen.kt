package org.example.frikollection_mobile_desktop.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.example.frikollection_mobile_desktop.register.resources.DatePickerDesktop
import org.example.frikollection_mobile_desktop.register.resources.LegalSection
import org.example.frikollection_mobile_desktop.register.resources.LegalTexts

@Composable
actual fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showDesktopDatePicker by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x66CCCCCC))
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        val maxContentWidth = if (maxWidth < 401.dp) maxWidth else 400.dp

        Column(
            modifier = Modifier
                .width(maxContentWidth)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "SIGN UP",
                style = MaterialTheme.typography.h5.copy(fontSize = 28.sp),
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = state.username,
                onValueChange = { newValue -> viewModel.update { it.copy(username = newValue) } },
                label = { Text("Username", style = MaterialTheme.typography.body1) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.firstName,
                onValueChange = { newValue -> viewModel.update { it.copy(firstName = newValue) } },
                label = { Text("First Name", style = MaterialTheme.typography.body1) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.lastName,
                onValueChange = { newValue -> viewModel.update { it.copy(lastName = newValue) } },
                label = { Text("Last Name", style = MaterialTheme.typography.body1) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { newValue -> viewModel.update { it.copy(email = newValue) } },
                label = { Text("Email", style = MaterialTheme.typography.body1) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { showDesktopDatePicker = true },
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color.Gray),
                color = MaterialTheme.colors.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (state.birthday.isNotBlank()) state.birthday else "Birthday",
                        color = if (state.birthday.isNotBlank()) MaterialTheme.colors.onSurface else Color.Gray,
                        style = MaterialTheme.typography.body1
                    )

                    Icon(
                        imageVector = if (state.birthday.isNotBlank()) Icons.Outlined.DateRange else Icons.Filled.DateRange,
                        contentDescription = "Select date",
                        tint = Color.Gray
                    )
                }
            }

            OutlinedTextField(
                value = state.password,
                onValueChange = { newValue -> viewModel.update { it.copy(password = newValue) } },
                label = { Text("Password", style = MaterialTheme.typography.body1) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Outlined.Lock else Icons.Filled.Lock,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { newValue -> viewModel.update { it.copy(confirmPassword = newValue) } },
                label = { Text("Confirm Password", style = MaterialTheme.typography.body1) },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Outlined.Lock else Icons.Filled.Lock,
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.acceptTerms,
                    onCheckedChange = {
                        viewModel.update { it.copy(acceptTerms = !state.acceptTerms) }
                    }
                )
                val termsText = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF1976D2))) {
                        append("Accept ")
                        pushStringAnnotation(tag = "TERMS", annotation = "terms_of_service")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Terms of Service")
                        }
                        pop()
                        append(" and agree to be bound by them")
                    }
                }
                ClickableText(
                    text = termsText,
                    style = MaterialTheme.typography.body1.copy(fontSize = 12.sp),
                    onClick = { offset ->
                        termsText.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                            .firstOrNull()?.let {
                                showTermsDialog = true
                            }
                    }
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.acceptPrivacy,
                    onCheckedChange = {
                        viewModel.update { it.copy(acceptPrivacy = !state.acceptPrivacy) }
                    }
                )
                val privacyText = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF1976D2))) {
                        append("I have received and reviewed the ")
                        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy_policy")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Privacy Policy")
                        }
                        pop()
                    }
                }
                ClickableText(
                    text = privacyText,
                    style = MaterialTheme.typography.body1.copy(fontSize = 12.sp),
                    onClick = { offset ->
                        privacyText.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                            .firstOrNull()?.let {
                                showPrivacyDialog = true
                            }
                    }
                )
            }

            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.error,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }


            Button(
                onClick = {
                    viewModel.submit {
                        viewModel.clearState()
                        onRegisterSuccess()
                    }
                },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("SIGN UP", color = Color.White, style = MaterialTheme.typography.subtitle1)
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Do you have an account?", fontSize = 12.sp, style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Log in",
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.body1,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }

    if (showDesktopDatePicker) {
        DatePickerDesktop(
            initialDate = state.birthday,
            onDateSelected = { selectedDate ->
                viewModel.update { it.copy(birthday = selectedDate) }
                showDesktopDatePicker = false
            },
            onDismissRequest = {
                showDesktopDatePicker = false
            }
        )
    }

    if (showTermsDialog) {
        LegalDialog(
            title = "Terms of Service",
            sections = LegalTexts.termsOfService,
            onAccept = {
                viewModel.update { it.copy(acceptTerms = true) }
                showTermsDialog = false
            },
            onDismiss = { showTermsDialog = false }
        )
    }

    if (showPrivacyDialog) {
        LegalDialog(
            title = "Privacy Policy",
            sections = LegalTexts.privacyPolicy,
            onAccept = {
                viewModel.update { it.copy(acceptPrivacy = true) }
                showPrivacyDialog = false
            },
            onDismiss = { showPrivacyDialog = false }
        )
    }
}

@Composable
fun LegalDialog(
    title: String,
    sections: List<LegalSection>,
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            elevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(min = 400.dp, max = 500.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.h6.copy(fontSize = 24.sp),
                        color = Color.Black
                    )
                }

                Divider(color = Color.Black, thickness = 0.5.dp)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    sections.forEach { section ->
                        Text(
                            text = section.title,
                            style = MaterialTheme.typography.body2.copy(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )

                        section.body.split("\n").forEach { line ->
                            Text(
                                text = line,
                                style = MaterialTheme.typography.body2.copy(
                                    fontSize = 12.sp
                                ),
                                color = Color.DarkGray,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.padding(start = if (section.isList) 16.dp else 0.dp, bottom = 4.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancel".uppercase(), color = Color.Gray)
                    }

                    Button(
                        onClick = onAccept,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Accept".uppercase(), color = Color.White)
                    }
                }
            }
        }
    }
}