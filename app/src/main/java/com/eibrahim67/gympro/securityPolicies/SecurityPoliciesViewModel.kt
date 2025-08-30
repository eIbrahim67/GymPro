package com.eibrahim67.gympro.securityPolicies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SecurityPoliciesViewModel : ViewModel() {
    private val _securityAndPoliciesContent = MutableStateFlow(
        """
        <b>GymPro</b> is committed to ensuring the <i>privacy, security, and safety</i> of all our users. By using the app, you agree to the following policies designed to protect your data and provide a safe community.<br/><br/>

        <b>1. Data Privacy</b><br/>
        - We collect only the information needed to provide and improve your fitness experience.<br/>
        - Your personal data will <i>never</i> be sold or shared without your consent.<br/>
        - All sensitive information is <b>encrypted</b> and stored securely.<br/><br/>

        <b>2. Account Security</b><br/>
        - Keep your login credentials confidential and use a <i>strong password</i>.<br/>
        - If suspicious activity is detected, reset your password immediately.<br/>
        - Contact our support team for any security concerns.<br/><br/>

        <b>3. Trainer and Trainee Policies</b><br/>
        - Trainers must provide accurate qualifications and maintain professionalism.<br/>
        - All users are expected to communicate respectfully.<br/>
        - <i>Harassment, abuse, or inappropriate content</i> will result in suspension.<br/><br/>

        <b>4. Payments and Subscriptions</b><br/>
        - All transactions are handled securely through trusted providers.<br/>
        - Subscription terms are transparent, and you can cancel anytime.<br/><br/>

        <b>5. Community Guidelines</b><br/>
        - Be respectful and supportive in all interactions.<br/>
        - Do not share offensive, harmful, or misleading content.<br/>
        - Repeated violations may lead to <b>account termination</b>.<br/><br/>

        <b>6. Changes to This Policy</b><br/>
        - Policies may be updated occasionally. Users will be notified of significant changes.<br/><br/>

        <b>7. Contact Us</b><br/>
        For questions or concerns, please reach out at:<br/>
        📧 <i>support@gympro.com</i><br/><br/>
    """.trimIndent()
    )
    val securityAndPoliciesContent: StateFlow<String> = _securityAndPoliciesContent

    fun loadSecurityAndPoliciesContent() {
        viewModelScope.launch {
            try {
                _securityAndPoliciesContent.value = securityAndPoliciesContent.value
            } catch (e: Exception) {
                Log.e("Security-ViewModel", "Error loading Security and Policies content")
            }
        }
    }
}