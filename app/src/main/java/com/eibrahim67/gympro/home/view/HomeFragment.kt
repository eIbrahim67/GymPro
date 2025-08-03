package com.eibrahim67.gympro.home.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.eibrahim67.gympro.R
import com.eibrahim67.gympro.core.data.local.preference.PreferenceManager
import com.eibrahim67.gympro.core.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.core.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.core.data.local.source.UserDatabase
import com.eibrahim67.gympro.core.data.remote.model.Workout
import com.eibrahim67.gympro.core.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.core.data.remote.source.RemoteDataSourceImpl
import com.eibrahim67.gympro.core.data.response.ResponseEI
import com.eibrahim67.gympro.core.utils.UtilsFunctions
import com.eibrahim67.gympro.core.utils.UtilsFunctions.createFailureResponse
import com.eibrahim67.gympro.databinding.FragmentHomeBinding
import com.eibrahim67.gympro.home.view.adapters.AdapterRVCategories
import com.eibrahim67.gympro.home.view.adapters.AdapterRVFeaturedPlans
import com.eibrahim67.gympro.home.view.adapters.AdapterRVOtherWorkouts
import com.eibrahim67.gympro.home.view.adapters.AdapterRVTrainers
import com.eibrahim67.gympro.home.view.bottomSheet.SeeMoreBottomSheet
import com.eibrahim67.gympro.home.viewModel.HomeViewModel
import com.eibrahim67.gympro.home.viewModel.HomeViewModelFactory
import com.eibrahim67.gympro.main.viewModel.MainViewModel
import com.eibrahim67.gympro.train.viewModel.TrainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapterRVFeaturedPlans = AdapterRVFeaturedPlans { id -> goToTrainPlan(id) }
    private val adapterRVTrainers = AdapterRVTrainers { id -> chatWithTrainer(id) }
    private val adapterRVCategories = AdapterRVCategories()
    private val adapterRVOtherWorkouts = AdapterRVOtherWorkouts()

    private val utils = UtilsFunctions

    lateinit var preferenceManager: PreferenceManager

    private var workoutNumber = -1
    private var exercisesNumber = -1
    private var repsNumber = -1
    private var todayNumOfTime = -1

    val repsMap = mutableMapOf<String, Int>()
    var list: List<Int>? = listOf<Int>()
    var workout: Workout? = null

    private val viewModel: HomeViewModel by viewModels {
        val remoteRepository =
            RemoteRepositoryImpl(RemoteDataSourceImpl(FirebaseFirestore.getInstance()))
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        HomeViewModelFactory(remoteRepository, userRepository)
    }

    private val sharedViewModel: MainViewModel by activityViewModels {
        val dao = UserDatabase.getDatabaseInstance(requireContext()).userDao()
        val localDateSource = LocalDateSourceImpl(dao)
        val userRepository = UserRepositoryImpl(localDateSource)
        TrainViewModelFactory(userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        updateUi()
        initObservers()
        initSetOnClickListener()
    }


    private fun initUi() {
        preferenceManager = PreferenceManager(requireContext())
        preferenceManager.resetAllProgress()
//        preferenceManager.setWorkoutDone(true)
//        preferenceManager.setDayNumber(12)
    }


    private fun updateUi() {
        binding.recyclerviewFeaturePlan.adapter = adapterRVFeaturedPlans
        binding.recyclerviewOurTrainers.adapter = adapterRVTrainers
        binding.beginTraining.text = if (todayNumOfTime > 0) "Let’s Finish This!"
        else if (preferenceManager.getWorkoutDone()) "We Finish Today!" else "Let’s Get Started!"
    }

    private fun initObservers() {

        viewModel.getMyTrainPlan()
        viewModel.myTrainPlan.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Loading -> {

                }

                is ResponseEI.Success -> {
                    binding.todayWorkoutTitle.text = "${response.data?.name}"
                    list = response.data?.workoutsIds
                    if (preferenceManager.getDayNumber() != getTodayKey()) {
                        list?.let {
                            when (preferenceManager.newDay(it.size)) {
                                0 -> {
                                    snackbar("It's a new day with new workout.")
                                }

                                1 -> {

                                    AlertDialog.Builder(requireContext())
                                        .setTitle("Unfinished Workout")
                                        .setMessage("It's a new day, but you didn't complete your workout yesterday. What would you like to do?")
                                        .setPositiveButton("Finish") { _, _ ->
                                            list?.let { preferenceManager.finishWorkout(it.size) }
                                            list?.let {
                                                workoutNumber =
                                                    preferenceManager.getWorkoutNumber(it.size)
                                            }
                                            list?.let { sharedViewModel.getWorkoutById(it[workoutNumber]) }
                                        }.setNegativeButton("Re-Train") { _, _ ->
                                            preferenceManager.reTrainWorkout()
                                            list?.let {
                                                workoutNumber =
                                                    preferenceManager.getWorkoutNumber(it.size)
                                            }
                                            list?.let { sharedViewModel.getWorkoutById(it[workoutNumber]) }
                                        }
                                        .setCancelable(false) // Optional: prevent dismiss by tapping outside
                                        .show()
                                }

                                2 -> {
                                    snackbar("You missed workout yesterday. Ready to catch up?")
                                }

                                else -> {}
                            }
                        }

                    }

                    list?.let { workoutNumber = preferenceManager.getWorkoutNumber(it.size) }
                    list?.let { sharedViewModel.getWorkoutById(it[workoutNumber]) }

                    if (preferenceManager.getWorkoutDone()) {

                        binding.workoutProgressText.text =
                            "${workoutNumber + 1}/${list?.size}\nWorkout"

                        list?.let {
                            binding.workoutProgressIndicator.progress =
                                (((workoutNumber + 1).toDouble() / it.size) * 100).toInt()
                        }

                    }

                }

                is ResponseEI.Failure -> {}
            }
        }

        sharedViewModel.workoutById.observe(viewLifecycleOwner) { resultWorkout ->
            when (resultWorkout) {
                is ResponseEI.Loading -> {
                    list?.let { workoutNumber = preferenceManager.getWorkoutNumber(it.size) }
                    exercisesNumber = preferenceManager.getExercisesNumber()
                    repsNumber = preferenceManager.getRepsNumber()
                    todayNumOfTime = preferenceManager.getTimeSpent()
                }

                is ResponseEI.Success -> {

                    workout = resultWorkout.data

                    workout?.let {
                        for (e in it.exerciseIds) {
                            sharedViewModel.getExerciseById(e)
                        }
                    }

                    workout?.let { binding.todayWorkoutName.text = "-> ${it.name}" }

                    binding.workoutProgressText.text = "${workoutNumber}/${list?.size}\nWorkout"

                    list?.let {
                        binding.workoutProgressIndicator.progress =
                            ((workoutNumber.toDouble() / it.size) * 100).toInt()
                    }


                    workout?.exerciseIds?.let {
                        binding.todayNumOfExercises.text =
                            "${exercisesNumber}/${it.size}\nExercises"
                    }

                    workout?.exerciseIds?.let {
                        binding.todayExercisesLinearProgressIndicator.progress =
                            ((exercisesNumber.toDouble() / it.size) * 100.0).toInt()
                    }


                    binding.todayNumOfTime.text =
                        "${todayNumOfTime}/${workout?.durationMinutes}\nMins"

                    workout?.let {
                        binding.todayTimeLinearProgressIndicator.progress =
                            ((todayNumOfTime.toDouble() / it.durationMinutes) * 100).toInt()
                    }


                }

                is ResponseEI.Failure -> createFailureResponse(resultWorkout, requireContext())
            }
        }


        sharedViewModel.exerciseById.observe(viewLifecycleOwner) { exercises ->
            when (exercises) {
                is ResponseEI.Success -> {
                    exercises.data?.let { exercise ->
                        repsMap[exercise.id.toString()] =
                            exercise.exerciseReps * exercise.exerciseSet ?: 0

                        binding.todayNumOfReps.text = "${repsNumber}/${repsMap.values.sum()}\nReps"

                        binding.todayRepsLinearProgressIndicator.progress =
                            ((repsNumber.toDouble() / repsMap.values.sum()) * 100).toInt()

                    }
                }

                is ResponseEI.Failure -> {
                    createFailureResponse(ResponseEI.Failure(exercises.reason), requireContext())
                }

                else -> {}
            }
        }

        viewModel.getCategories()
        viewModel.categories.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Success -> response.data?.let { adapterRVCategories.submitList(it) }
                is ResponseEI.Failure -> utils.createFailureResponse(response, requireContext())
                else -> {}
            }
        }


        sharedViewModel.getTrainPlans()
        sharedViewModel.trainPlans.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Success -> adapterRVFeaturedPlans.submitList(response.data)
                is ResponseEI.Failure -> utils.createFailureResponse(response, requireContext())
                else -> {}
            }
        }

        viewModel.getCoaches()
        viewModel.coaches.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Success -> adapterRVTrainers.submitList(response.data)
                is ResponseEI.Failure -> utils.createFailureResponse(response, requireContext())
                else -> {}
            }
        }

        viewModel.getWorkouts()
        viewModel.workouts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseEI.Success -> adapterRVOtherWorkouts.submitList(response.data)
                is ResponseEI.Failure -> utils.createFailureResponse(response, requireContext())
                else -> {}
            }
        }

        viewModel.getCurrentDate()
        viewModel.currentDate.observe(viewLifecycleOwner) { date ->
            when (date) {
                is ResponseEI.Success -> binding.currentDate.text = date.data
                is ResponseEI.Failure -> utils.createFailureResponse(date, requireContext())
                else -> {}
            }
        }

        viewModel.getHelloSate()
        viewModel.helloSate.observe(viewLifecycleOwner) { date ->
            when (date) {
                is ResponseEI.Success -> binding.textViewHello.text = date.data
                is ResponseEI.Failure -> utils.createFailureResponse(date, requireContext())
                else -> {}
            }
        }
    }

    private fun initSetOnClickListener() {
        binding.beginTraining.setOnClickListener {
            sharedViewModel.navigateRightTo(R.id.action_train)
        }

        binding.chatbotLayout.setOnClickListener {
            sharedViewModel.navigateRightTo(R.id.action_action_home_to_chatbot)
        }

        binding.profileLayout.setOnClickListener {
            sharedViewModel.navigateLeftTo(R.id.action_profile)
        }

        val seeMoreBottomSheet = SeeMoreBottomSheet{id-> goToTrainPlan(id)}

        binding.textViewFeaturePlanSeeMore.setOnClickListener {

            seeMoreBottomSheet.show(requireActivity().supportFragmentManager, "SeeMoreBottomSheet")

        }
    }

    fun getTodayKey(): Int {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return dateFormat.format(Date()).toInt()
    }

    private fun goToTrainPlan(id: Int) {
        sharedViewModel.setTrainPlanId(id)
        sharedViewModel.navigateRightTo(R.id.action_showTrainPlan)
    }

    private fun chatWithTrainer(id: Int) {
        sharedViewModel.setChatWithId(id)
        sharedViewModel.navigateRightTo(R.id.action_chat)
    }

    fun snackbar(msg: String) = Snackbar.make(
        requireView(), msg, Snackbar.LENGTH_INDEFINITE
    ).setAction("OK") { }.show()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
