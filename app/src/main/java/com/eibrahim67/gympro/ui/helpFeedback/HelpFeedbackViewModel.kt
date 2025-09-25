package com.eibrahim67.gympro.ui.helpFeedback

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HelpFeedbackViewModel : ViewModel() {
    private val _helpAndFeedbackContent = MutableStateFlow(
        """
        <b>Need Help?</b><br/>
        At <b>GymPro</b>, we are here to make your fitness journey <i>smooth, motivating, and hassle-free</i>. If you encounter any issues or have questions, you can always reach out to us.<br/><br/>

        <b>1. Frequently Asked Questions (FAQ)</b><br/>
        - <b>How do I create a training plan?</b><br/>
        Go to the <i>Create Training Plan</i> page, select your target muscles, set workout days, and save your plan.<br/><br/>
        - <b>How can I connect with a trainer?</b><br/>
        Subscribe to a trainer from the <i>Trainer Subscription</i> feature and start chatting with them on the <i>Chat Page</i>.<br/><br/>
        - <b>Where can I see my progress?</b><br/>
        Navigate to the <i>My Progress</i> page for detailed charts, or check <i>Most Improved Muscles</i> for rankings.<br/><br/>

        <b>2. Contact Support</b><br/>
        If you face technical issues, billing problems, or need further assistance, please email us at:<br/>
        📧 <i>support@gympro.com</i><br/><br/>

        <b>3. Share Feedback</b><br/>
        We value your input! Share your ideas, suggestions, or feature requests directly through this page or by contacting our support team. Your feedback helps us improve GymPro and deliver the best possible experience.<br/><br/>

        <b>4. Report a Problem</b><br/>
        If you notice any bugs, incorrect data, or inappropriate content, please report it immediately so we can take action.<br/><br/>

        Thank you for being part of the <b>GymPro community</b>. Together, we’re building a stronger, healthier future!<br/><br/>
    """.trimIndent()
    )
    val helpAndFeedbackContent: StateFlow<String> = _helpAndFeedbackContent

    fun loadHelpAndFeedbackContent() {
        viewModelScope.launch {
            try {
                _helpAndFeedbackContent.value = helpAndFeedbackContent.value
            } catch (e: Exception) {
                Log.e("HelpFeedback-ViewModel", "Error loading Help & Feedback content")
            }
        }
    }
}