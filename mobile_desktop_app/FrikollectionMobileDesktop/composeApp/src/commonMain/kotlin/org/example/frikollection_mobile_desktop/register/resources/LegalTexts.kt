package org.example.frikollection_mobile_desktop.register.resources

data class LegalSection(
    val title: String,
    val body: String,
    val isList: Boolean = false
)

object LegalTexts {
    val termsOfService = listOf(
        LegalSection(
            "Introduction",
            "Welcome to Frikollection! By using our app, you agree to the following terms."
        ),
        LegalSection(
            "1. Use of the App",
            "You agree to use Frikollection only for lawful purposes and in a manner that does not infringe the rights of others."
        ),
        LegalSection(
            "2. User Accounts",
            "You are responsible for maintaining the confidentiality of your account credentials. You agree not to share your account with others."
        ),
        LegalSection(
            "3. Content Ownership",
            "All content uploaded by users (e.x. images, descriptions, lists) remains the property of the user. However, by uploading, you grant us a non-exclusive right to display that content within the app."
        ),
        LegalSection(
            "4. Prohibited Conduct",
            """
            - Post offensive, illegal, or harmful content.
            - Harass, threaten, or impersonate other users.
            - Attempt to reverse-engineer or interfere with the app's operation.
            """.trimIndent(),
            isList = true
        ),
        LegalSection(
            "5. Modifications and Termination",
            "We reserve the right to modify or discontinue the app at any time. We may also terminate your access if you violate these terms."
        ),
        LegalSection(
            "6. Disclaimer",
            "Frikollection is provided \"as is\", without warranties of any kind. We do not guarantee uninterrupted access or that the app will be error-free."
        ),
        LegalSection(
            "7. Contact",
            "For questions, contact us at: support@frikollection.app"
        )
    )

    val privacyPolicy = listOf(
        LegalSection(
            "Introduction",
            "This Privacy Policy explains how Frikollection collects, uses, and protects your personal information."
        ),
        LegalSection(
            "1. Information We Collect",
            """
            - Email address and username
            - Profile information (nickname, avatar, bio)
            - Collection data (items, tags, notes)
            """.trimIndent(),
            isList = true
        ),
        LegalSection(
            "2. How We Use Your Information",
            """
            - Provide access to your collections
            - Personalize your experience
            - Show public collections to other users (if enabled)
            - Send occasional service-related notifications
            """.trimIndent(),
            isList = true
        ),
        LegalSection(
            "3. Data Sharing",
            "We do not sell or share your personal data with third parties. However, data may be stored on external services (e.g. cloud storage providers) necessary to operate the app."
        ),
        LegalSection(
            "4. Data Retention",
            "Your data is retained as long as your account is active. You can request deletion of your account and associated data at any time."
        ),
        LegalSection(
            "5. Security",
            "We implement standard security measures to protect your data, but no system is 100% secure."
        ),
        LegalSection(
            "6. Your Rights",
            "You can access, modify, or delete your data at any time from your profile settings or by contacting us."
        ),
        LegalSection(
            "7. Contact",
            "If you have questions about this policy, contact us at: privacy@frikollection.app"
        )
    )
}