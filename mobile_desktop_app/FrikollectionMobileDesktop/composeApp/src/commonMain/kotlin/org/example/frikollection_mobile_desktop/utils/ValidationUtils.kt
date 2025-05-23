object ValidationUtils {
    fun validateNickname(nickname: String): String? {
        return when {
            nickname.length < 2 -> "Minimum 2 characters"
            nickname.length > 16 -> "Maximum 16 characters"
            else -> null
        }
    }

    fun validateName(name: String): String? {
        return if (name.isBlank()) "Required" else null
    }

    fun validatePhoneNumber(phone: String): String? {
        return when {
            phone.isBlank() -> null
            !phone.matches(Regex("^\\d{9}$")) -> "Must be 9 digits"
            else -> null
        }
    }

    fun validateBirthday(birthday: String): String? {
        return when {
            birthday.isBlank() -> null
            !birthday.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) -> "Invalid format (yyyy-MM-dd)"
            else -> null
        }
    }

    fun validateNewPassword(password: String): String? {
        return when {
            password.isBlank() -> null
            password.length < 6 -> "Minimum 6 characters"
            !password.any { it.isUpperCase() } -> "Must contain uppercase letter"
            !password.any { it.isLowerCase() } -> "Must contain lowercase letter"
            !password.any { it.isDigit() } -> "Must contain number"
            else -> null
        }
    }

    fun validateConfirmNewPassword(newPassword: String, confirmNewPassword: String): String? {
        return when {
            newPassword != confirmNewPassword -> "New passwords aren't equals"
            else -> null
        }
    }
}