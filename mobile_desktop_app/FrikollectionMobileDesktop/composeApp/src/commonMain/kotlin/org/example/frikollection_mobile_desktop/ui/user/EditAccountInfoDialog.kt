package org.example.frikollection_mobile_desktop.ui.user

import ValidationUtils.validateBirthday
import ValidationUtils.validateConfirmNewPassword
import ValidationUtils.validateName
import ValidationUtils.validateNewPassword
import ValidationUtils.validateNickname
import ValidationUtils.validatePhoneNumber
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.example.frikollection_mobile_desktop.account.AccountViewModel
import org.example.frikollection_mobile_desktop.models.user.UpdateUserDto
import org.example.frikollection_mobile_desktop.models.user.UserProfileDto

@Composable
fun EditAccountInfoDialog(
    viewModel: AccountViewModel,
    initial: UserProfileDto,
    onDismiss: () -> Unit,
    onSave: (UpdateUserDto) -> Unit
) {
    var nickname by remember { mutableStateOf(initial.nickname ?: "") }
    var firstName by remember { mutableStateOf(initial.firstName ?: "") }
    var lastName by remember { mutableStateOf(initial.lastName ?: "") }
    var phoneNumber by remember { mutableStateOf(initial.phoneNumber ?: "") }
    var birthday by remember { mutableStateOf(initial.birthday ?: "") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    var errors by remember { mutableStateOf(mapOf<String, String>()) }

    var nicknameFocus by remember { mutableStateOf(false) }
    var firstNameFocus by remember { mutableStateOf(false) }
    var lastNameFocus by remember { mutableStateOf(false) }
    var phoneFocus by remember { mutableStateOf(false) }
    var birthdayFocus by remember { mutableStateOf(false) }
    var currentPasswordFocus by remember { mutableStateOf(false) }
    var newPasswordFocus by remember { mutableStateOf(false) }
    var confirmNewPasswordFocus by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(Color.White, RoundedCornerShape(20.dp))
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0D47A1), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Edit Account Info", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                }

                Column(modifier = Modifier.padding(24.dp)) {
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = {
                            nickname = it
                        },
                        label = { Text("Nickname") },
                        singleLine = true,
                        isError = errors.containsKey("nickname"),
                        modifier = Modifier.fillMaxWidth().onFocusChanged {
                            nicknameFocus = it.isFocused
                            if (!it.isFocused) {
                                validateNickname(nickname)?.let { msg ->
                                    errors = errors + ("nickname" to msg)
                                } ?: run { errors = errors - "nickname" }
                            }
                        }
                    )
                    errors["nickname"]?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

                    OutlinedTextField(
                        value = firstName,
                        onValueChange = {
                            firstName = it
                        },
                        label = { Text("First Name") },
                        singleLine = true,
                        isError = errors.containsKey("firstName"),
                        modifier = Modifier.fillMaxWidth().onFocusChanged {
                            firstNameFocus = it.isFocused
                            if (!it.isFocused) {
                                validateName(firstName)?.let { msg ->
                                    errors = errors + ("firstName" to msg)
                                } ?: run { errors = errors - "firstName" }
                            }
                        }
                    )
                    errors["firstName"]?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = {
                            lastName = it
                        },
                        label = { Text("Last Name") },
                        singleLine = true,
                        isError = errors.containsKey("lastName"),
                        modifier = Modifier.fillMaxWidth().onFocusChanged {
                            lastNameFocus = it.isFocused
                            if (!it.isFocused) {
                                validateName(lastName)?.let { msg ->
                                    errors = errors + ("lastName" to msg)
                                } ?: run { errors = errors - "lastName" }
                            }
                        }
                    )
                    errors["lastName"]?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it
                        },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = errors.containsKey("phoneNumber"),
                        modifier = Modifier.fillMaxWidth().onFocusChanged {
                            phoneFocus = it.isFocused
                            if (!it.isFocused) {
                                validatePhoneNumber(phoneNumber)?.let { msg ->
                                    errors = errors + ("phoneNumber" to msg)
                                } ?: run { errors = errors - "phoneNumber" }
                            }
                        }
                    )
                    errors["phoneNumber"]?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

                    OutlinedTextField(
                        value = birthday,
                        onValueChange = {
                            birthday = it
                        },
                        label = { Text("Birthday (yyyy-MM-dd)") },
                        singleLine = true,
                        isError = errors.containsKey("birthday"),
                        modifier = Modifier.fillMaxWidth().onFocusChanged {
                            birthdayFocus = it.isFocused
                            if (!it.isFocused) {
                                validateBirthday(birthday)?.let { msg ->
                                    errors = errors + ("birthday" to msg)
                                } ?: run { errors = errors - "birthday" }
                            }
                        }
                    )
                    errors["birthday"]?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = {
                            currentPassword = it
                        },
                        label = { Text("Current Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = errors.containsKey("currentPassword"),
                        modifier = Modifier.fillMaxWidth().onFocusChanged {
                            currentPasswordFocus = it.isFocused
                            if (!it.isFocused) {
                                if (currentPassword.isBlank()) {
                                    errors = errors + ("currentPassword" to "La contrasenya actual és obligatòria.")
                                } else {
                                    errors = errors - "currentPassword"
                                }
                            }
                        }
                    )
                    errors["currentPassword"]?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = {
                            newPassword = it
                        },
                        label = { Text("New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = errors.containsKey("newPassword"),
                        modifier = Modifier.fillMaxWidth().onFocusChanged {
                            newPasswordFocus = it.isFocused
                            if (!it.isFocused) {
                                validateNewPassword(newPassword)?.let { msg ->
                                    errors = errors + ("newPassword" to msg)
                                } ?: run { errors = errors - "newPassword" }
                            }
                        }
                    )
                    errors["newPassword"]?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = confirmNewPassword,
                        onValueChange = {
                            confirmNewPassword = it
                        },
                        label = { Text("Confirm New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = errors.containsKey("confirmNewPassword"),
                        modifier = Modifier.fillMaxWidth().onFocusChanged {
                            confirmNewPasswordFocus = it.isFocused
                            if (!it.isFocused) {
                                validateConfirmNewPassword(
                                    newPassword,
                                    confirmNewPassword
                                )?.let { msg -> errors = errors + ("confirmNewPassword" to msg) }
                                    ?: run { errors = errors - "confirmNewPassword" }
                            }
                        }
                    )
                    errors["confirmeNewPassword"]?.let {
                        Text(
                            it,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0D47A1), RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancel", color = Color.White, modifier = Modifier.background(Color(0xFF0D47A1)))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (errors.isEmpty()) {
                                viewModel.updateUserInfo(
                                    UpdateUserDto(
                                        nickname = nickname.ifBlank { null },
                                        firstName = firstName.ifBlank { null },
                                        lastName = lastName.ifBlank { null },
                                        phoneNumber = phoneNumber.ifBlank { null },
                                        birthday = birthday.ifBlank { null },
                                        currentPassword = currentPassword.ifBlank { null },
                                        newPassword = newPassword.ifBlank { null }
                                    )
                                )
                                viewModel.loadUserProfile()
                                showSuccessDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Text("Save", color = Color(0xFF0D47A1))
                    }
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Profile Update") },
            text = { Text("Your profile has been updated succesfully.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0D47A1))
                ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }
}
