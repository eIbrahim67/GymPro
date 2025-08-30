package com.eibrahim67.gympro.about

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AboutViewModel : ViewModel() {
    private val _aboutUsContent = MutableStateFlow(
        """
        <b>GymPro</b> is a <i>comprehensive fitness tracking platform</i> built to help individuals achieve their <b>health and fitness goals</b> through personalized training, expert guidance, and powerful progress tracking tools.<br/><br/>

        Our mission is simple: to make fitness <b>accessible, motivating, and results-driven</b> for everyone. Whether you're just starting your fitness journey or are an experienced athlete, GymPro gives you the tools and support you need to succeed.<br/><br/>

        <b>Why Choose GymPro?</b><br/>
        - <b>Personalized Training Plans:</b> Choose or create plans tailored to your lifestyle, goals, and fitness level.<br/>
        - <b>Workout Tracking:</b> Record reps, sets, weights, and monitor performance over time.<br/>
        - <b>Certified Trainers:</b> Connect with experts who guide you step-by-step toward your goals.<br/>
        - <b>AI Chatbot Assistant:</b> Get instant answers to workout and nutrition questions.<br/>
        - <b>Progress Insights:</b> Visual reports to track improvements across different muscle groups.<br/>
        - <b>Trainer Opportunities:</b> Become a trainer and earn money by coaching trainees.<br/><br/>

        <b>Our Vision</b><br/>
        At GymPro, we believe fitness is more than workouts—it’s about building <i>confidence, discipline, and a healthier lifestyle</i>. We aim to create a global fitness community where users and trainers can connect, grow, and achieve success together.<br/><br/>

        <b>Our Promise</b><br/>
        - To keep your data <b>safe and private</b>.<br/>
        - To provide a <b>seamless, user-friendly</b> experience.<br/>
        - To continuously improve with <i>new features and innovations</i> that support your journey.<br/><br/>

        Start your fitness journey today with <b>GymPro</b> and unlock the best version of yourself!<br/><br/>
    """.trimIndent()
    )
    val aboutUsContent: StateFlow<String> = _aboutUsContent

    fun loadAboutUsContent() {
        viewModelScope.launch {
            try {
                _aboutUsContent.value = aboutUsContent.value
            } catch (e: Exception) {
                Log.e("AboutUs-ViewModel", "Error loading About Us content")
            }
        }
    }
}